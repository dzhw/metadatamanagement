package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

/**
 * This queue collects automatic dara updates in a queue and handles.
 * @author Daniel Katzberg
 *
 */
@Service
@Slf4j
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
  private RelatedPublicationRepository relatedPublicationRepository;
  
  /**
   * The Dara Service for updating Studies on Dara.
   */
  @Autowired
  private DaraService daraService;
  
  /**
   * Do checks with the related publication before delete or save operations.
   * 
   * @param relatedPublication the updated or deleted related 
   *     publication before the delete or save process 
   */
  @HandleBeforeCreate
  @HandleBeforeSave
  @HandleBeforeDelete
  public void onRelatedPublicationBeforeSavedOrDelete(RelatedPublication relatedPublication) {
    log.debug("Before Related Publication Save/Create/Delete:");
    
    RelatedPublication oldPublication = this.relatedPublicationRepository
        .findById(relatedPublication.getId());  
    
    for (String studyId : relatedPublication.getStudyIds()) {
      
      Study study = studyRepository.findOne(studyId);
     
      if (oldPublication == null) {        
        if (study != null) {
          log.error("Did not find Related Publication:" + relatedPublication.getId());
          
          DataAcquisitionProject dataAcquisitionProject =
              this.projectRepository.findOne(study.getDataAcquisitionProjectId());
          
          if (dataAcquisitionProject != null
              && dataAcquisitionProject.getRelease() != null) {
            this.enqueue(study.getDataAcquisitionProjectId());
          }
        }  
        //no old publication, no known study id? -> Do nothing!        
      } else {
        log.debug("Found Related Publication:" + relatedPublication.getId());
        log.debug("Old Source Reference: " + oldPublication.getSourceReference());
        log.debug("New Source Reference: " + relatedPublication.getSourceReference());
        
        if (study != null) {
          DataAcquisitionProject dataAcquisitionProject =
              this.projectRepository.findOne(study.getDataAcquisitionProjectId());
        
          //Source Reference is not equals
          if (!(oldPublication.getSourceReference()
              .equals(relatedPublication.getSourceReference())) 
              && dataAcquisitionProject != null
              && dataAcquisitionProject.getRelease() != null) {
            this.enqueue(dataAcquisitionProject.getId());
          }
          //Source Reference is equal. -> Do nothing.
        } else {
          log.error("Found Related Publication:" + relatedPublication.getId() 
              + " but unknown StudyId: " + studyId);
        }
      }
    }
  }
    
  /**
   * Attach one item to the queue.
   * 
   * @param projectId The id of the data acquisition project to be updated.
   */
  public void enqueue(String projectId) {
    try {      
      DaraUpdateQueueItem existingItem = queueItemRepository.findOneByProjectId(projectId);
      if (existingItem != null) {
        queueItemRepository.delete(existingItem.getId());
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
      executeQueueItem(lockedItems);

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

  /**
   * Execute locked items from the MongoDB Queue Repository.
   * 
   * @param lockedItems A list of locked queues items.
   */
  private void executeQueueItem(List<DaraUpdateQueueItem> lockedItems) {
    for (DaraUpdateQueueItem lockedItem : lockedItems) {
      try {
        this.daraService.registerOrUpdateProjectToDara(lockedItem.getProjectId());
      } catch (IOException | TemplateException e) {
        log.error("Error at registration to Dara: " + e.getMessage());
      }
    }
    
    // finally delete the queue items
    queueItemRepository.delete(lockedItems);
  }
}
