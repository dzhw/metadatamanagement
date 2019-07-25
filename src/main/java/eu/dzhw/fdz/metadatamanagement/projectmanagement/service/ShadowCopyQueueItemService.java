package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the creation of shadow copies by triggering a {@link ShadowCopyingStartedEvent}. This service
 * monitors the database for existing {@link ShadowCopyQueueItem} and creates shadow copies for each
 * one starting with the oldest entry.
 */
@Service
@Slf4j
public class ShadowCopyQueueItemService {

  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  private ApplicationEventPublisher applicationEventPublisher;

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private UserDetailsService userDetailsService;

  /**
   * Create a new instance.
   */
  public ShadowCopyQueueItemService(ApplicationEventPublisher applicationEventPublisher,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      ShadowCopyQueueItemRepository shadowCopyQueueItemRepository,
      DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService,
      UserDetailsService userDetailsService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
    this.shadowCopyQueueItemRepository = shadowCopyQueueItemRepository;
    this.dataAcquisitionProjectVersionsService = dataAcquisitionProjectVersionsService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Create a new shadow copy queue item.
   * 
   * @param dataAcquisitionProjectId Id of project for which a shadow copy should be created
   * @param shadowCopyVersion The shadow copy version
   */
  public void createShadowCopyTask(String dataAcquisitionProjectId, String shadowCopyVersion) {
    ShadowCopyQueueItem queueItem = new ShadowCopyQueueItem();

    Optional<ShadowCopyQueueItem> taskItem =
        shadowCopyQueueItemRepository.findByDataAcquisitionProjectIdAndShadowCopyVersion(
            dataAcquisitionProjectId, shadowCopyVersion);

    taskItem.ifPresent(
        shadowCopyQueueItem -> shadowCopyQueueItemRepository.delete(shadowCopyQueueItem));

    queueItem.setDataAcquisitionProjectId(dataAcquisitionProjectId);
    queueItem.setShadowCopyVersion(shadowCopyVersion);
    queueItem.setCreatedBy(SecurityUtils.getCurrentUserLogin());
    shadowCopyQueueItemRepository.save(queueItem);
  }

  /**
   * Emits {@link ShadowCopyingStartedEvent} for each entry of the collection at a fixed rate.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void createShadowCopies() {
    LocalDateTime updateStartTime = LocalDateTime.now();
    shadowCopyQueueItemRepository.lockAllUnlockedOrExpiredItems(updateStartTime, jvmId);
    List<ShadowCopyQueueItem> tasks =
        shadowCopyQueueItemRepository.findOldestLockedItems(updateStartTime, jvmId);
    log.debug("Creating shadow copies for {} queued items.", tasks.size());
    tasks.forEach(task -> {
      try {
        setupSecurityContext(task);
        String dataAcquisitionProjectId = task.getDataAcquisitionProjectId();
        String releaseVersion = task.getShadowCopyVersion();
        Optional<DataAcquisitionProject> dataAcquisitionProjectOpt =
            dataAcquisitionProjectRepository.findById(dataAcquisitionProjectId);
        if (dataAcquisitionProjectOpt.isPresent()) {
          DataAcquisitionProject dataAcquisitionProject = dataAcquisitionProjectOpt.get();
          emitProjectReleasedEvent(dataAcquisitionProject, releaseVersion);
        } else {
          log.warn("A shadow copy task was scheduled for project {}, but it could not be found!",
              dataAcquisitionProjectId);
        }
        shadowCopyQueueItemRepository.delete(task);
      } finally {
        clearSecurityContext();
      }
    });
    log.debug("Finished creating shadow copies.");
  }

  private void setupSecurityContext(ShadowCopyQueueItem shadowCopyQueueItem) {
    String username = shadowCopyQueueItem.getCreatedBy();
    try {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
          userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(token);
    } catch (UsernameNotFoundException e) {
      throw new IllegalStateException("User " + username + " created a shadow copy task for "
          + "project " + shadowCopyQueueItem.getDataAcquisitionProjectId() + ", but this user "
          + "could not be found!", e);
    }
  }

  private void clearSecurityContext() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  private void emitProjectReleasedEvent(DataAcquisitionProject dataAcquisitionProject,
      String releaseVersion) {
    Release currentRelease = null;
    if (dataAcquisitionProject.getRelease() != null
        && dataAcquisitionProject.getRelease().getVersion().equals(releaseVersion)) {
      currentRelease = dataAcquisitionProject.getRelease();
    } else {
      currentRelease =
          dataAcquisitionProjectVersionsService.findLastRelease(dataAcquisitionProject.getId());
    }
    assert currentRelease.getVersion().equals(releaseVersion);

    Release previousRelease = dataAcquisitionProjectVersionsService
        .findPreviousRelease(dataAcquisitionProject.getId(), currentRelease);
    String previousVersion;

    if (previousRelease != null) {
      previousVersion = previousRelease.getVersion();
    } else {
      previousVersion = null;
    }
    this.applicationEventPublisher.publishEvent(new ShadowCopyingStartedEvent(this,
        dataAcquisitionProject.getId(), releaseVersion, previousVersion));
  }
}
