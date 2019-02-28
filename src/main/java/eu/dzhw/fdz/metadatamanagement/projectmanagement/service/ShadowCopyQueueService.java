package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Handles the creation of shadow copies by triggering a {@link ProjectReleasedEvent}. This
 * service monitors the database for existing {@link ShadowCopyQueueItem} and creates shadow copies
 * for each one starting with the oldest entry.
 */
@Service
@Slf4j
public class ShadowCopyQueueService {

  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  private ApplicationEventPublisher applicationEventPublisher;

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private ShadowCopyQueueRepository shadowCopyQueueRepository;

  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private UserDetailsService userDetailsService;

  /**
   * Create a new instance.
   */
  public ShadowCopyQueueService(ApplicationEventPublisher applicationEventPublisher,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      ShadowCopyQueueRepository shadowCopyQueueRepository,
      DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService,
      UserDetailsService userDetailsService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
    this.shadowCopyQueueRepository = shadowCopyQueueRepository;
    this.dataAcquisitionProjectVersionsService = dataAcquisitionProjectVersionsService;
    this.userDetailsService = userDetailsService;
  }

  /**
   * Create a new shadow copy queue item.
   * @param dataAcquisitionProjectId Id of project for which a shadow copy should be created
   * @param shadowCopyVersion        The shadow copy version
   */
  public void createShadowCopyTask(String dataAcquisitionProjectId, String shadowCopyVersion) {
    ShadowCopyQueueItem queueItem = new ShadowCopyQueueItem();

    Optional<ShadowCopyQueueItem> taskItem = shadowCopyQueueRepository
        .findByDataAcquisitionProjectIdAndShadowCopyVersion(dataAcquisitionProjectId,
            shadowCopyVersion);

    taskItem.ifPresent(shadowCopyQueueItem -> shadowCopyQueueRepository
        .delete(shadowCopyQueueItem));

    queueItem.setDataAcquisitionProjectId(dataAcquisitionProjectId);
    queueItem.setShadowCopyVersion(shadowCopyVersion);
    queueItem.setUsername(SecurityUtils.getCurrentUserLogin());
    shadowCopyQueueRepository.save(queueItem);
  }

  /**
   * Emits {@link ProjectReleasedEvent} for each entry of the collection at a fixed rate.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void createShadowCopies() {
    LocalDateTime updateStartTime = LocalDateTime.now();
    shadowCopyQueueRepository.lockAllUnlockedOrExpiredItems(updateStartTime, jvmId);
    List<ShadowCopyQueueItem> tasks = shadowCopyQueueRepository
        .findOldestLockedItems(updateStartTime, jvmId);
    log.debug("Creating shadow copies for {} queued items.", tasks.size());
    tasks.forEach(task -> {
      try {
        setupSecurityContext(task);
        String dataAcquisitionProjectId = task.getDataAcquisitionProjectId();
        String shadowCopyVersion = task.getShadowCopyVersion();
        Optional<DataAcquisitionProject> dataAcquisitionProjectOpt =
            dataAcquisitionProjectRepository.findById(dataAcquisitionProjectId);
        if (dataAcquisitionProjectOpt.isPresent()) {
          DataAcquisitionProject dataAcquisitionProject = dataAcquisitionProjectOpt.get();
          emitProjectReleasedEvent(dataAcquisitionProject);
          emitProjectSavedEvent(dataAcquisitionProjectId, shadowCopyVersion);
          updateShadowCopyPredecessor(dataAcquisitionProject, shadowCopyVersion);
        } else {
          log.warn("A shadow copy task was scheduled for project {}, but it could not be found!",
              dataAcquisitionProjectId);
        }
        shadowCopyQueueRepository.delete(task);
      } finally {
        clearSecurityContext();
      }
    });
    log.debug("Finished creating shadow copies.");
  }

  private void updateShadowCopyPredecessor(DataAcquisitionProject dataAcquisitionProject,
                                           String shadowCopyVersion) {
    Release previousRelease = dataAcquisitionProjectVersionsService
        .findPreviousRelease(dataAcquisitionProject.getId(), dataAcquisitionProject.getRelease());
    if (previousRelease != null) {
      String lastVersion = previousRelease.getVersion();
      if (!shadowCopyVersion.equals(lastVersion)) {
        Optional<DataAcquisitionProject> projectOpt = dataAcquisitionProjectRepository
            .findById(dataAcquisitionProject.getId() + "-" + lastVersion);
        projectOpt.ifPresent(project -> applicationEventPublisher
            .publishEvent(new AfterSaveEvent(project)));
      }
    }
  }

  private void setupSecurityContext(ShadowCopyQueueItem shadowCopyQueueItem) {
    String username = shadowCopyQueueItem.getUsername();
    try {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
          userDetails.getUsername(), userDetails.getPassword(),userDetails.getAuthorities());
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

  // Trigger update methods in domain services so they queue document updates for ElasticSearch
  private void emitProjectSavedEvent(String dataAcquisitionProjectId, String shadowCopyVersion) {
    String shadowCopyId = dataAcquisitionProjectId + "-" + shadowCopyVersion;
    Optional<DataAcquisitionProject> shadowCopyOpt = dataAcquisitionProjectRepository
        .findById(shadowCopyId);

    if (shadowCopyOpt.isPresent()) {
      AfterSaveEvent saveEvent = new AfterSaveEvent(shadowCopyOpt.get());
      applicationEventPublisher.publishEvent(saveEvent);
    } else {
      log.warn("{} created a shadow copy for project with id {}, but it's shadow copy version "
              + "with id {} could not be found!", getClass().getSimpleName(),
          dataAcquisitionProjectId, shadowCopyId);
    }
  }

  private void emitProjectReleasedEvent(DataAcquisitionProject dataAcquisitionProject) {
    Release previousRelease = dataAcquisitionProjectVersionsService
        .findPreviousRelease(dataAcquisitionProject.getId(),
            dataAcquisitionProject.getRelease());
    String previousVersion;

    if (previousRelease != null) {
      previousVersion = previousRelease.getVersion();
    } else {
      previousVersion = null;
    }
    this.applicationEventPublisher.publishEvent(new ProjectReleasedEvent(this,
        dataAcquisitionProject, previousVersion));
  }
}
