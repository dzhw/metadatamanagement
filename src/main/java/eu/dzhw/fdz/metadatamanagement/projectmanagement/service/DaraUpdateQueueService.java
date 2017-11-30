package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import eu.dzhw.fdz.metadatamanagement.mailmanagement.service.MailService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.UserRepository;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;
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
  private RelatedPublicationRepository relatedPublicationRepository;
  
  @Autowired
  private UserRepository userRepository;
  
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
  public void onRelatedPublicationBeforeSavedOrDeleted(RelatedPublication relatedPublication) {
    RelatedPublication oldPublication = this.relatedPublicationRepository
        .findOne(relatedPublication.getId());
    
    if (oldPublication != null) {      
      List<String> deletedStudyIds = new ArrayList<>(oldPublication.getStudyIds());
      deletedStudyIds.removeAll(relatedPublication.getStudyIds());
      enqueueStudiesIfProjectIsReleased(deletedStudyIds);
      
      List<String> addedStudyIds = new ArrayList<>(relatedPublication.getStudyIds());
      addedStudyIds.removeAll(oldPublication.getStudyIds());
      enqueueStudiesIfProjectIsReleased(addedStudyIds);
      
      if (!oldPublication.getSourceReference().equals(relatedPublication.getSourceReference())) {
        enqueueStudiesIfProjectIsReleased(relatedPublication.getStudyIds());
      }
    } else {
      enqueueStudiesIfProjectIsReleased(relatedPublication.getStudyIds());
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
  
  private void enqueueStudiesIfProjectIsReleased(Collection<String> studyIds) {
    for (String studyId : studyIds) {
      Study study = studyRepository.findOne(studyId);
      if (study != null) {
        DataAcquisitionProject dataAcquisitionProject =
            this.projectRepository.findOne(study.getDataAcquisitionProjectId());
        
        if (dataAcquisitionProject != null
            && dataAcquisitionProject.getRelease() != null) {
          this.enqueue(study.getDataAcquisitionProjectId());
        }
      } else {
        log.error("Unable to find study with ID {}", studyId);
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
        this.daraService.registerOrUpdateProjectToDara(lockedItem.getProjectId());
      } catch (IOException | TemplateException | HttpClientErrorException e) {
        log.error("Error at registration to Dara: " + e.getMessage());
        // do not delete the queue item (has to be retried later)
        MailService mailService = new MailService();
        List<User> admins = userRepository.findAllByAuthoritiesContaining(
            new Authority(AuthoritiesConstants.ADMIN));
        mailService.sendMailOnDaraAutomaticUpdateError(admins, lockedItem.getProjectId());
        return;
      }
    }
    
    // finally delete the queue items
    queueItemRepository.delete(lockedItems);
  }
}
