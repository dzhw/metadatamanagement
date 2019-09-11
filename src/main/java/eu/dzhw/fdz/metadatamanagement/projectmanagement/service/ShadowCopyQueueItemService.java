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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the creation of shadow copies by triggering a {@link ShadowCopyingStartedEvent}. This
 * service monitors the database for existing {@link ShadowCopyQueueItem} and creates shadow copies
 * for each one starting with the oldest entry.
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
   * @param release The release object of the project which has been released.
   */
  public void scheduleShadowCopyCreation(String dataAcquisitionProjectId, Release release) {
    createShadowCopyQueueItem(dataAcquisitionProjectId, release, Action.CREATE);
  }

  private void createShadowCopyQueueItem(String dataAcquisitionProjectId, Release release,
      Action action) {
    ShadowCopyQueueItem queueItem = new ShadowCopyQueueItem();

    Optional<ShadowCopyQueueItem> taskItem =
        shadowCopyQueueItemRepository.findByDataAcquisitionProjectIdAndReleaseVersion(
            dataAcquisitionProjectId, release.getVersion());

    taskItem.ifPresent(
        shadowCopyQueueItem -> shadowCopyQueueItemRepository.delete(shadowCopyQueueItem));

    queueItem.setDataAcquisitionProjectId(dataAcquisitionProjectId);
    queueItem.setRelease(release);
    queueItem.setCreatedBy(SecurityUtils.getCurrentUserLogin());
    queueItem.setAction(action);
    shadowCopyQueueItemRepository.save(queueItem);
  }

  /**
   * Create a new shadow copy queue item for hiding the shadow copies of the given version.
   * 
   * @param dataAcquisitionProjectId Id of project for which the shadow copies shall be hidden.
   * @param release The release object of the project which has been released.
   */
  public void scheduleShadowCopyHiding(String dataAcquisitionProjectId, Release release) {
    createShadowCopyQueueItem(dataAcquisitionProjectId, release, Action.HIDE);
  }

  /**
   * Create a new shadow copy queue item for unhiding the shadow copies of the given version.
   * 
   * @param dataAcquisitionProjectId Id of project for which the shadow copies shall be unhidden.
   * @param release The release object of the project which has been released.
   */
  public void scheduleShadowCopyUnhiding(String dataAcquisitionProjectId, Release release) {
    createShadowCopyQueueItem(dataAcquisitionProjectId, release, Action.UNHIDE);
  }

  /**
   * Emits {@link ShadowCopyingStartedEvent} for each entry of the collection at a fixed rate.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void executeShadowCopyActions() {
    LocalDateTime updateStartTime = LocalDateTime.now();
    shadowCopyQueueItemRepository.lockAllUnlockedOrExpiredItems(updateStartTime, jvmId);
    List<ShadowCopyQueueItem> tasks =
        shadowCopyQueueItemRepository.findOldestLockedItems(updateStartTime, jvmId);
    log.debug("Executing shadow copy actions for {} queued items.", tasks.size());
    tasks.forEach(task -> {
      try {
        setupSecurityContext(task);
        String dataAcquisitionProjectId = task.getDataAcquisitionProjectId();
        Release release = task.getRelease();
        Optional<DataAcquisitionProject> dataAcquisitionProjectOpt =
            dataAcquisitionProjectRepository.findById(dataAcquisitionProjectId);
        if (dataAcquisitionProjectOpt.isPresent()) {
          DataAcquisitionProject dataAcquisitionProject = dataAcquisitionProjectOpt.get();
          switch (task.getAction()) {
            case CREATE:
              Optional<DataAcquisitionProject> existingShadow = dataAcquisitionProjectRepository
                  .findById(dataAcquisitionProjectId + "-" + release.getVersion());
              String previousReleaseVersion =
                  getPreviousReleaseVersion(dataAcquisitionProject, release);
              emitShadowCopyingStartedEvent(dataAcquisitionProject, release, previousReleaseVersion,
                  task.getAction());
              emitShadowCopyingEndedEvent(dataAcquisitionProject, release, previousReleaseVersion,
                  existingShadow.isPresent(), task.getAction());
              break;
            case HIDE:
            case UNHIDE:
              emitShadowCopyingStartedEvent(dataAcquisitionProject, release, null,
                  task.getAction());
              emitShadowCopyingEndedEvent(dataAcquisitionProject, release, null, true,
                  task.getAction());
              break;
            default:
              throw new IllegalArgumentException(
                  task.getAction() + " has not been implemented yet!");
          }

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

  private void emitShadowCopyingEndedEvent(DataAcquisitionProject dataAcquisitionProject,
      Release release, String previousReleaseVersion, boolean isRerelease, Action action) {
    this.applicationEventPublisher.publishEvent(new ShadowCopyingEndedEvent(this,
        dataAcquisitionProject.getId(), release, previousReleaseVersion, isRerelease, action));
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

  private String getPreviousReleaseVersion(DataAcquisitionProject dataAcquisitionProject,
      Release currentRelease) {
    Release previousRelease = dataAcquisitionProjectVersionsService
        .findPreviousRelease(dataAcquisitionProject.getId(), currentRelease);
    String previousVersion;

    if (previousRelease != null) {
      previousVersion = previousRelease.getVersion();
    } else {
      previousVersion = null;
    }

    return previousVersion;
  }

  private void emitShadowCopyingStartedEvent(DataAcquisitionProject dataAcquisitionProject,
      Release release, String previousReleaseVersion, Action action) {
    this.applicationEventPublisher.publishEvent(new ShadowCopyingStartedEvent(this,
        dataAcquisitionProject.getId(), release, previousReleaseVersion, action));
  }

  /**
   * Get the action which is currently performed for the given shadow identified by the given
   * params.
   * 
   * @param dataAcquisitionProjectId masterId of the project
   * @param version the version of the project
   * @return The current action if the shadow is still in the queue.
   */
  public Action findCurrentAction(String dataAcquisitionProjectId, String version) {
    Optional<ShadowCopyQueueItem> queueItem = shadowCopyQueueItemRepository
        .findByDataAcquisitionProjectIdAndReleaseVersion(dataAcquisitionProjectId, version);
    if (queueItem.isPresent()) {
      return queueItem.get().getAction();
    }
    return null;
  }
}
