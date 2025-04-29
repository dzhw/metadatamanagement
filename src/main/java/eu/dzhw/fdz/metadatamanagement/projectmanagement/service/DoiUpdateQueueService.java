package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This queue collects automatic DOI updates in a queue and handles.
 *
 * @author Daniel Katzberg
 *
 */
@Service
@Slf4j
@RepositoryEventHandler
@RequiredArgsConstructor
public class DoiUpdateQueueService {
  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  private final DaraUpdateQueueItemRepository queueItemRepository;

  private final DataAcquisitionProjectRepository projectRepository;

  private final DataPackageRepository dataPackageRepository;

  private final AnalysisPackageRepository analysisPackageRepository;

  private final RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  private final UserRepository userRepository;

  private final MailService mailService;

  private final DataCiteService dataCiteService;

  /**
   * Update dataPackage metadata at DataCite if necessary.
   *
   * @param relatedPublication the changed publication
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    enqueueDataPackagesIfProjectIsCurrentlyReleasedToDara(
        relatedPublicationChangesProvider.getAddedDataPackageIds(relatedPublication.getId()));
    enqueueDataPackagesIfProjectIsCurrentlyReleasedToDara(
        relatedPublicationChangesProvider.getDeletedDataPackageIds(relatedPublication.getId()));
    if (relatedPublicationChangesProvider.hasChangesRelevantForDara(relatedPublication.getId())) {
      enqueueDataPackagesIfProjectIsCurrentlyReleasedToDara(
          relatedPublicationChangesProvider.getAffectedDataPackageIds(relatedPublication.getId()));
    }
    enqueueAnalysisPackagesIfProjectIsCurrentlyReleasedToDara(
        relatedPublicationChangesProvider.getAddedAnalysisPackageIds(relatedPublication.getId()));
    enqueueAnalysisPackagesIfProjectIsCurrentlyReleasedToDara(
        relatedPublicationChangesProvider.getDeletedAnalysisPackageIds(relatedPublication.getId()));
    if (relatedPublicationChangesProvider.hasChangesRelevantForDara(relatedPublication.getId())) {
      enqueueAnalysisPackagesIfProjectIsCurrentlyReleasedToDara(relatedPublicationChangesProvider
          .getAffectedAnalysisPackageIds(relatedPublication.getId()));
    }
  }

  /**
   * Attach one item to the queue.
   *
   * @param projectId The id of the data acquisition project to be updated.
   */
  private void enqueue(String projectId) {
    try {
      DaraUpdateQueueItem existingItem = queueItemRepository.findOneByProjectId(projectId);
      if (existingItem != null) {
        queueItemRepository.deleteById(existingItem.getId());
      }
      queueItemRepository.insert(new DaraUpdateQueueItem(projectId));
    } catch (DuplicateKeyException ex) {
      log.debug("Ignoring attempt to enqueue a duplicate action.");
    }
  }

  /**
   * Process the update queue every minute.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void processAllQueueItems() {
    log.debug("Starting processing of DoiUpdateQueue...");
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItems(updateStart, jvmId);

    List<DaraUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItems(jvmId, updateStart);

    while (!lockedItems.isEmpty()) {
      executeQueueItems(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItems(jvmId, updateStart);
    }
    log.debug("Finished processing of DoiUpdateQueue...");
  }

  /**
   * Deletes all queue elements in the MongoDB Queue Repository.
   */
  public void clearQueue() {
    queueItemRepository.deleteAll();
  }

  private void enqueueDataPackagesIfProjectIsCurrentlyReleasedToDara(
      Collection<String> dataPackageIds) {
    for (String dataPackageId : dataPackageIds) {
      DataPackage dataPackage = dataPackageRepository.findById(dataPackageId).orElse(null);
      if (dataPackage != null) {
        DataAcquisitionProject dataAcquisitionProject =
            this.projectRepository.findById(dataPackage.getDataAcquisitionProjectId()).orElse(null);

        if (dataAcquisitionProject != null && dataAcquisitionProject.getRelease() != null
            && Version.valueOf(dataAcquisitionProject.getRelease().getVersion())
                .greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
          this.enqueue(dataPackage.getDataAcquisitionProjectId());
        }
      } else {
        log.warn("Unable to find dataPackage with ID {}", dataPackageId);
      }
    }
  }

  private void enqueueAnalysisPackagesIfProjectIsCurrentlyReleasedToDara(
      Collection<String> analysisPackageIds) {
    for (String analysisPackageId : analysisPackageIds) {
      AnalysisPackage analysisPackage =
          analysisPackageRepository.findById(analysisPackageId).orElse(null);
      if (analysisPackage != null) {
        DataAcquisitionProject dataAcquisitionProject = this.projectRepository
            .findById(analysisPackage.getDataAcquisitionProjectId()).orElse(null);

        if (dataAcquisitionProject != null && dataAcquisitionProject.getRelease() != null
            && Version.valueOf(dataAcquisitionProject.getRelease().getVersion())
                .greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
          this.enqueue(analysisPackage.getDataAcquisitionProjectId());
        }
      } else {
        log.warn("Unable to find analysisPackage with ID {}", analysisPackageId);
      }
    }
  }

  /**
   * Execute locked items from the MongoDB Queue Repository.
   *
   * @param lockedItems A list of locked queues items.
   */
  private void executeQueueItems(List<DaraUpdateQueueItem> lockedItems) {
    for (DaraUpdateQueueItem lockedItem : lockedItems) {
      try {
        HttpStatus dataCiteResponseStatus =
            this.dataCiteService.registerOrUpdateProjectToDataCite(lockedItem.getProjectId());
        if (!dataCiteResponseStatus.is2xxSuccessful()) {
          handleDataCiteCommunicationError(lockedItem);
          continue;
        }
      } catch (Exception e) {
        log.error("Error at registration to DataCite: " + e.getMessage());
        // do not delete the queue item (has to be retried later)
        handleDataCiteCommunicationError(lockedItem);
        continue;
      }
    }

    // finally delete the queue items
    queueItemRepository.deleteAll(lockedItems);
  }

  private void handleDataCiteCommunicationError(DaraUpdateQueueItem lockedItem) {
    List<User> admins =
        userRepository.findAllByAuthoritiesContaining(new Authority(AuthoritiesConstants.ADMIN));
    mailService.sendMailOnDataCiteAutomaticUpdateError(admins, lockedItem.getProjectId());
    this.unlock(lockedItem);
  }

  private void unlock(DaraUpdateQueueItem item) {
    item.setUpdateStartedAt(null);
    item.setUpdateStartedBy(null);
    this.queueItemRepository.save(item);
  }
}
