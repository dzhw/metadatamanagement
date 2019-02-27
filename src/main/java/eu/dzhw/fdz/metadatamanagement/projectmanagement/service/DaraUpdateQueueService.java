package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.github.zafarkhaja.semver.Version;

import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * This queue collects automatic dara updates in a queue and handles.
 * @author Daniel Katzberg
 *
 */
@Service
@Slf4j
@RepositoryEventHandler
public class DaraUpdateQueueService {
  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  /**
   * MongoDB Repository with gets all queue elements for the synchronizations of the Dara DB.
   */
  @Autowired
  private DaraUpdateQueueItemRepository queueItemRepository;
  
  @Autowired
  private DataAcquisitionProjectRepository projectRepository;
  
  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private MailService mailService;
  
  /**
   * The Dara Service for updating Studies on Dara.
   */
  @Autowired
  private DaraService daraService;
  
  /**
   * Update study metadata at dara if necessary.
   * @param relatedPublication the changed publication
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    enqueueStudiesIfProjectIsCurrentlyReleasedToDara(relatedPublicationChangesProvider
        .getAddedStudyIds(relatedPublication.getId()));
    enqueueStudiesIfProjectIsCurrentlyReleasedToDara(relatedPublicationChangesProvider
        .getDeletedStudyIds(relatedPublication.getId()));
    if (relatedPublicationChangesProvider.hasChangesRelevantForDara(relatedPublication.getId())) {
      enqueueStudiesIfProjectIsCurrentlyReleasedToDara(
          relatedPublicationChangesProvider.getAffectedStudyIds(relatedPublication.getId()));      
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
    log.info("Starting processing of DaraUpdateQueue...");
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItems(updateStart, jvmId);

    List<DaraUpdateQueueItem> lockedItems = 
        queueItemRepository.findOldestLockedItems(jvmId, updateStart);

    while (!lockedItems.isEmpty()) {
      executeQueueItems(lockedItems);      

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItems(jvmId, updateStart);
    }
    log.info("Finished processing of DaraUpdateQueue...");
  }

  /**
   * Deletes all queue elements in the MongoDB Queue Repository.
   */
  public void clearQueue() {
    queueItemRepository.deleteAll();
  }
  
  private void enqueueStudiesIfProjectIsCurrentlyReleasedToDara(Collection<String> studyIds) {
    for (String studyId : studyIds) {
      Study study = studyRepository.findById(studyId).orElse(null);
      if (study != null) {
        DataAcquisitionProject dataAcquisitionProject =
            this.projectRepository.findById(study.getDataAcquisitionProjectId())
            .orElse(null);
        
        if (dataAcquisitionProject != null
            && dataAcquisitionProject.getRelease() != null
            && Version.valueOf(dataAcquisitionProject.getRelease().getVersion())
            .greaterThanOrEqualTo(Version.valueOf("1.0.0"))) {
          this.enqueue(study.getDataAcquisitionProjectId());
        }
      } else {
        log.warn("Unable to find study with ID {}", studyId);
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
        HttpStatus responseStatus = this.daraService
            .registerOrUpdateProjectToDara(lockedItem.getProjectId());
        if (!responseStatus.is2xxSuccessful()) {
          handleDaraCommunicationError(lockedItem);
          continue;
        }
      } catch (Exception e) {
        log.error("Error at registration to Dara: " + e.getMessage());
        // do not delete the queue item (has to be retried later)
        handleDaraCommunicationError(lockedItem);
        continue;
      }
    }
    
    // finally delete the queue items
    queueItemRepository.deleteAll(lockedItems);
  }

  private void handleDaraCommunicationError(DaraUpdateQueueItem lockedItem) {
    List<User> admins = userRepository.findAllByAuthoritiesContaining(
        new Authority(AuthoritiesConstants.ADMIN));
    mailService.sendMailOnDaraAutomaticUpdateError(admins, lockedItem.getProjectId());
    this.unlock(lockedItem);
  }
  
  private void unlock(DaraUpdateQueueItem item) {
    item.setUpdateStartedAt(null);
    item.setUpdateStartedBy(null);
    this.queueItemRepository.save(item);
  }
}
