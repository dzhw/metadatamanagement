package eu.dzhw.fdz.metadatamanagement.projectmanagement.service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DaraUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DaraUpdateQueueItemRepository;
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
  
  /**
   * The Dara Service for updating Studies on Dara.
   */
  @Autowired
  private DaraService daraService;
    
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
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
    // finally delete the queue items
    queueItemRepository.delete(lockedItems);
  }
}
