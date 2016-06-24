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

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestion;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.AtomicQuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.AtomicQuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

/**
 * Service which manages asynchronous Elasticsearch updates as a FIFO queue.
 * 
 * @author René Reitmann
 */
@Service
public class ElasticsearchUpdateQueueService {

  private final Logger logger = LoggerFactory.getLogger(ElasticsearchUpdateQueueService.class);

  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  /**
   * MongoDB Repository with gets all queue elements for the synchronizations 
   * of the Elasticsearch DB.
   */
  @Inject
  private ElasticsearchUpdateQueueItemRepository queueItemRepository;

  /**
   * Repository for the variables for updating them.
   */
  @Inject
  private VariableRepository variableRepository;
  
  @Inject
  private DataSetRepository dataSetRepository;

  /**
   * Repository for the surveys for updating them.
   */
  @Inject
  private SurveyRepository surveyRepository;
  
  /**
   * DAO for Elasticsearch synchronization.
   */
  @Inject
  private AtomicQuestionRepository atomicQuestionRepository;
  
  @Inject
  private ElasticsearchDao elasticsearchDao;

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
  
  /**
   * Deletes all queue elements in the MongoDB Queue Repository.
   */
  public void clearQueue() {
    queueItemRepository.deleteAll();
  }

  /**
   * Looks elements in the MongoDb and update them in the Elasticsearch DB.
   * @param updateStart Starttime for updates.
   */
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

  /**
   * Execute locked items from the MongoDB Queue Repository.
   * @param lockedItems A list of locked queues items.
   */
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
    elasticsearchDao.executeBulk(bulkBuilder.build());
    
    // finally delete the queue items
    queueItemRepository.delete(lockedItems);
  }

  /**
   * Adds a Deletes Action to the bulk builder.
   * @param lockedItem a locked item.
   * @param bulkBuilder for building an add action.
   */
  private void addDeleteActions(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
      bulkBuilder.addAction(new Delete.Builder(lockedItem.getDocumentId())
          .index(index.getIndexName())
          .type(lockedItem.getDocumentType().name())
          .build());
    }
  }

  /**
   * Add a update / insert action to the bulk builder.
   * @param lockedItem A locked item.
   * @param bulkBuilder The bulk builder for building update / insert actions.
   */
  private void addUpsertActions(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    switch (lockedItem.getDocumentType()) {
      case variables:
        addUpsertActionForVariable(lockedItem, bulkBuilder);
        break;
      case surveys:
        addUpsertActionForSurvey(lockedItem, bulkBuilder);
        break;
      case data_sets:
        addUpsertActionForDataSet(lockedItem, bulkBuilder);
        break;
      case atomic_questions:
        addUpsertActionForAtomicQuestion(lockedItem, bulkBuilder);
        break;  
      default:
        throw new NotImplementedException("Processing queue item with type "
            + lockedItem.getDocumentType() + " has not been implemented!");
    }
  }
  
  private void addUpsertActionForAtomicQuestion(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    AtomicQuestion atomicQuestion = atomicQuestionRepository.findOne(lockedItem.getDocumentId());
    if (atomicQuestion != null) {
      for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
        AtomicQuestionSearchDocument searchDocument =
            new AtomicQuestionSearchDocument(atomicQuestion, index);

        bulkBuilder.addAction(new Index.Builder(searchDocument)
            .index(index.getIndexName())
            .type(lockedItem.getDocumentType().name())
            .id(searchDocument.getId())
            .build());
      }
    }
  }
  
  private void addUpsertActionForDataSet(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    DataSet dataSet = dataSetRepository.findOne(lockedItem.getDocumentId());
    if (dataSet != null) {
      Iterable<Survey> surveys = null;
      if (dataSet.getSurveyIds() != null) {
        surveys = surveyRepository.findAll(dataSet.getSurveyIds());
      }
      for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
        DataSetSearchDocument searchDocument =
            new DataSetSearchDocument(dataSet, surveys, index);

        bulkBuilder.addAction(new Index.Builder(searchDocument)
            .index(index.getIndexName())
            .type(lockedItem.getDocumentType().name())
            .id(searchDocument.getId())
            .build());
      }
    }
  }

  private void addUpsertActionForSurvey(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Survey survey = surveyRepository.findOne(lockedItem.getDocumentId());
    if (survey != null) {
      for (ElasticsearchIndices index : ElasticsearchIndices.values()) {
        SurveySearchDocument searchDocument =
            new SurveySearchDocument(survey, index);

        bulkBuilder.addAction(new Index.Builder(searchDocument)
            .index(index.getIndexName())
            .type(lockedItem.getDocumentType().name())
            .id(searchDocument.getId())
            .build());
      }
    }
  }

  /**
   * This method creates for the variable repository update / insert actions.
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
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
