package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchAdminDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.search.document.VariableSearchDocument;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

/**
 * Service which manages asynchronous Elasticsearch updates.
 * 
 * @author René Reitmann
 */
@Service
public class ElasticsearchUpdateQueueService {

  private final Logger logger = LoggerFactory.getLogger(ElasticsearchUpdateQueueService.class);

  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  @Inject
  private ElasticsearchUpdateQueueItemRepository queueItemRepository;

  @Inject
  private VariableRepository variableRepository;

  @Inject
  private SurveyRepository surveyRepository;
  
  @Inject
  private ElasticsearchAdminDao elasticsearchAdminDao;

  /**
   * Attach one item to the queue.
   * 
   * @param documentId The id of the document to be updated or deleted
   * @param documentType the type of the document to be updated or deleted
   * @param action delete or update
   */
  public void enqueue(String documentId, ElasticsearchType documentType,
      ElasticsearchUpdateQueueAction action) {
    try {
      queueItemRepository
        .insert(new ElasticsearchUpdateQueueItem(documentId, documentType, action));
    } catch (DuplicateKeyException ex) {
      logger.debug("Ignoring attempt to enqueue a duplicate action.");
    }
  }

  /**
   * Attach multiple items to the queue.
   * 
   * @param documentIds The ids of the documents to be updated or deleted
   * @param documentType the type of the document to be updated or deleted
   * @param action delete or update
   */
  public void enqueue(Iterable<String> documentIds, ElasticsearchType documentType,
      ElasticsearchUpdateQueueAction action) {
    for (String documentId : documentIds) {
      this.enqueue(documentId, documentType, action);
    }
  }

  /**
   * Process the update queue every 5 minutes.
   */
  @Scheduled(fixedRate = 1000 * 60 * 5, initialDelay = 1000 * 60 * 5)
  public void processQueue() {
    logger.info("Starting processing of ElasticsearchUpdateQueue...");
    LocalDateTime updateStart = LocalDateTime.now();

    lockUpdateQueueItems(updateStart);

    List<ElasticsearchUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItems(jvmId, updateStart);

    while (!lockedItems.isEmpty()) {
      executeQueueItemActions(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItems(jvmId, updateStart);
    }
    logger.info("Finished processing of ElasticsearchUpdateQueue...");
  }

  private void lockUpdateQueueItems(LocalDateTime updateStart) {
    List<ElasticsearchUpdateQueueItem> unlockedItems =
        queueItemRepository.findUnlockedOrExpiredItems();

    while (!unlockedItems.isEmpty()) {
      for (ElasticsearchUpdateQueueItem item : unlockedItems) {
        item.setUpdateStartedAt(updateStart);
        item.setUpdateStartedBy(jvmId);
        try {
          queueItemRepository.save(item);
        } catch (OptimisticLockingFailureException ex) {
          logger.debug("Queue item will be processed by a different cluster instance.");
        }
      }

      // check if there are more unlocked items
      unlockedItems = queueItemRepository.findUnlockedOrExpiredItems();
    }
  }

  private void executeQueueItemActions(List<ElasticsearchUpdateQueueItem> lockedItems) {
    Bulk.Builder bulkBuilder = new Bulk.Builder();
    for (ElasticsearchUpdateQueueItem lockedItem : lockedItems) {
      switch (lockedItem.getAction()) {
        case DELETE:
          addDeleteActions(lockedItem, bulkBuilder);
          break;
        case UPSERT:
          addUpsertActions(lockedItem, bulkBuilder);
          break;
        default:
          throw new NotImplementedException("Processing queue item with action "
              + lockedItem.getAction() + " has not been implemented!");
      }
    }

    // execute the bulk update/delete
    elasticsearchAdminDao.executeBulk(bulkBuilder.build());
    
    // finally delete the queue items
    queueItemRepository.delete(lockedItems);
  }

  private void addDeleteActions(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
      bulkBuilder.addAction(new Delete.Builder(lockedItem.getDocumentId())
          .index(index.getIndexName())
          .type(lockedItem.getDocumentType().name())
          .build());
    }
  }

  private void addUpsertActions(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    switch (lockedItem.getDocumentType()) {
      case variables:
        addUpsertActionForVariable(lockedItem, bulkBuilder);
        break;
      default:
        throw new NotImplementedException("Processing queue item with type "
            + lockedItem.getDocumentType() + " has not been implemented!");
    }
  }

  private void addUpsertActionForVariable(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Variable variable = variableRepository.findOne(lockedItem.getDocumentId());
    if (variable != null) {
      Iterable<Survey> surveys = null;
      if (variable.getSurveyIds() != null) {
        surveys = surveyRepository.findAll(variable.getSurveyIds());
      }
      for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
        VariableSearchDocument searchDocument =
            new VariableSearchDocument(variable, surveys, index);

        bulkBuilder.addAction(new Index.Builder(searchDocument)
            .index(index.getIndexName())
            .type(lockedItem.getDocumentType().name())
            .id(searchDocument.getId())
            .build());
      }
    }
  }
}
