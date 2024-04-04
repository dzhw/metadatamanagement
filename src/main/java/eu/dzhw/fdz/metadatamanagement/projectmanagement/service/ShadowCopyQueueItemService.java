package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem.Action;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.SecurityUtils;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.UserInformationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handles the creation of shadow copies by triggering a {@link ShadowCopyingStartedEvent}. This
 * service monitors the database for existing {@link ShadowCopyQueueItem} and creates shadow copies
 * for each one starting with the oldest entry.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ShadowCopyQueueItemService {

  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  private final ApplicationEventPublisher applicationEventPublisher;

  private final DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private final ShadowCopyQueueItemRepository shadowCopyQueueItemRepository;

  private final DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final UserInformationProvider userInformationProvider;

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
   * Create a new shadow copy queue item for deleting the shadow copies of the given version.
   *
   * @param dataAcquisitionProjectId (Master-) Id of project for which the shadow copies shall be
   *        deleted.
   * @param release The release object containing the version of the project which shall be deleted.
   */
  public void scheduleShadowCopyDeletion(String dataAcquisitionProjectId, Release release) {
    createShadowCopyQueueItem(dataAcquisitionProjectId, release, Action.DELETE);
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
        Optional<DataAcquisitionProject> dataAcquisitionProjectOpt;
        if (task.getAction().equals(ShadowCopyQueueItem.Action.DELETE)) {
          dataAcquisitionProjectOpt = dataAcquisitionProjectRepository
              .findById(dataAcquisitionProjectId + "-" + release.getVersion());
        } else {
          dataAcquisitionProjectOpt =
              dataAcquisitionProjectRepository.findById(dataAcquisitionProjectId);
        }
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
              // check if its a real re-release or a release after a pre-release
              boolean isReleaseAfterPreRelease = existingShadow.isPresent()
                  && existingShadow.get().getRelease().getIsPreRelease();
              emitShadowCopyingEndedEvent(dataAcquisitionProject, release, previousReleaseVersion,
                  existingShadow.isPresent(), task.getAction(), isReleaseAfterPreRelease);
              break;
            case HIDE:
            case UNHIDE:
              emitShadowCopyingStartedEvent(dataAcquisitionProject, release, null,
                  task.getAction());
              emitShadowCopyingEndedEvent(dataAcquisitionProject, release, null, true,
                  task.getAction(), false);
              break;
            case DELETE:
              emitShadowCopyingStartedEvent(dataAcquisitionProject, release, null,
                  task.getAction());
              emitShadowCopyingEndedEvent(dataAcquisitionProject, release, null, false,
                  task.getAction(), false);
              break;
            default:
              throw new IllegalArgumentException(
                  task.getAction() + " has not been implemented yet!");
          }

        } else {
          log.warn("A shadow copy task was scheduled for project {}, but it could not be found!",
              dataAcquisitionProjectId);
        }
        elasticsearchUpdateQueueService.processAllQueueItems();
        shadowCopyQueueItemRepository.delete(task);
      } finally {
        clearSecurityContext();
      }
    });
    log.debug("Finished creating shadow copies.");
  }

  private void emitShadowCopyingEndedEvent(DataAcquisitionProject dataAcquisitionProject,
      Release release, String previousReleaseVersion, boolean isRerelease,
      Action action, boolean isReleaseAfterPreRelease) {
    this.applicationEventPublisher
        .publishEvent(new ShadowCopyingEndedEvent(this, dataAcquisitionProject.getMasterId(),
            release, previousReleaseVersion, isRerelease, action, isReleaseAfterPreRelease));
  }

  private void setupSecurityContext(ShadowCopyQueueItem shadowCopyQueueItem) {
    String username = shadowCopyQueueItem.getCreatedBy();
    try {
      userInformationProvider.switchToUser(username);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("User " + username + " created a shadow copy task for "
          + "project " + shadowCopyQueueItem.getDataAcquisitionProjectId() + ", but this user "
          + "could not be found!", e);
    }
  }

  private void clearSecurityContext() {
    userInformationProvider.switchToUser(null);
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
        dataAcquisitionProject.getMasterId(), release, previousReleaseVersion, action));
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
