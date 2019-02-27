package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.ShadowCopyQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

  private static final Sort CREATED_DATE_ASC = Sort.by("createdDate").ascending();

  private ApplicationEventPublisher applicationEventPublisher;

  private DataAcquisitionProjectRepository dataAcquisitionProjectRepository;

  private ShadowCopyQueueRepository shadowCopyQueueRepository;

  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

  /**
   * Create a new instance.
   */
  public ShadowCopyQueueService(ApplicationEventPublisher applicationEventPublisher,
      DataAcquisitionProjectRepository dataAcquisitionProjectRepository,
      ShadowCopyQueueRepository shadowCopyQueueRepository,
      DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.dataAcquisitionProjectRepository = dataAcquisitionProjectRepository;
    this.shadowCopyQueueRepository = shadowCopyQueueRepository;
    this.dataAcquisitionProjectVersionsService = dataAcquisitionProjectVersionsService;
  }

  /**
   * Create a new shadow copy queue item.
   * @param dataAcquisitionProjectId Id of project for which a shadow copy should be created
   * @param shadowCopyVersion The shadow copy version
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
    shadowCopyQueueRepository.save(queueItem);
  }

  /**
   * Emits {@link ProjectReleasedEvent} for each entry of the collection at a fixed rate.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void createShadowCopies() {
    List<ShadowCopyQueueItem> tasks = shadowCopyQueueRepository.findAll(CREATED_DATE_ASC);
    log.debug("Creating shadow copies for {} queued items.", tasks.size());
    tasks.forEach(task -> {
      String dataAcquisitionProjectId = task.getDataAcquisitionProjectId();
      Optional<DataAcquisitionProject> dataAcquisitionProjectOpt = dataAcquisitionProjectRepository
          .findById(dataAcquisitionProjectId);
      if (dataAcquisitionProjectOpt.isPresent()) {
        emitProjectReleasedEvent(dataAcquisitionProjectOpt.get());
      } else {
        log.warn("A shadow copy task was scheduled for project {}, but it could not be found!",
            dataAcquisitionProjectId);
      }
      shadowCopyQueueRepository.delete(task);
    });
    log.debug("Finished creating shadow copies.");
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
