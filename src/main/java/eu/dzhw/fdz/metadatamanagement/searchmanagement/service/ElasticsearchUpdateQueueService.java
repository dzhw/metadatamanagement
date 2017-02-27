package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedPublicationSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import io.searchbox.core.Bulk;
import io.searchbox.core.Bulk.Builder;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

/**
 * Service which manages asynchronous Elasticsearch updates as a FIFO queue. Inserting an item into
 * the queue which already exists will remove the existing one and insert the new item at the end of
 * the queue.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
public class ElasticsearchUpdateQueueService {

  private final Logger logger = LoggerFactory.getLogger(ElasticsearchUpdateQueueService.class);

  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  /**
   * MongoDB Repository with gets all queue elements for the synchronizations of the Elasticsearch
   * DB.
   */
  @Autowired
  private ElasticsearchUpdateQueueItemRepository queueItemRepository;

  /**
   * Repository for the variables for updating them.
   */
  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private DataSetRepository dataSetRepository;

  /**
   * Repository for the surveys for updating them.
   */
  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private StudyRepository studyRepository;

  @Autowired
  private RelatedPublicationRepository relatedPublicationRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
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
      ElasticsearchUpdateQueueItem existingItem = queueItemRepository
          .findOneByDocumentTypeAndDocumentIdAndAction(documentType, documentId, action);
      if (existingItem != null) {
        queueItemRepository.delete(existingItem.getId());
      }
      queueItemRepository
        .insert(new ElasticsearchUpdateQueueItem(documentId, documentType, action));
    } catch (DuplicateKeyException ex) {
      logger.debug("Ignoring attempt to enqueue a duplicate action.");
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
   * 
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
   * 
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
   * 
   * @param lockedItem a locked item.
   * @param bulkBuilder for building an add action.
   */
  private void addDeleteActions(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    bulkBuilder
        .addAction(new Delete.Builder(lockedItem.getDocumentId())
            .index(lockedItem.getDocumentType().name())
            .type(lockedItem.getDocumentType().name())
            .build());   
  }

  /**
   * Add a update / insert action to the bulk builder.
   * 
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
      case questions:
        addUpsertActionForQuestion(lockedItem, bulkBuilder);
        break;
      case studies:
        addUpsertActionForStudy(lockedItem, bulkBuilder);
        break;
      case related_publications:
        addUpsertActionForRelatedPublication(lockedItem, bulkBuilder);
        break;
      case instruments:
        addUpsertActionForInstrument(lockedItem, bulkBuilder);
        break;
      default:
        throw new NotImplementedException("Processing queue item with type "
            + lockedItem.getDocumentType() + " has not been implemented!");
    }
  }

  private void addUpsertActionForInstrument(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Instrument instrument = instrumentRepository.findOne(lockedItem.getDocumentId());

    if (instrument != null) {
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(instrument.getStudyId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (instrument.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(instrument.getSurveyIds());        
      }
      List<QuestionSubDocumentProjection> questions = questionRepository
          .findSubDocumentsByInstrumentId(instrument.getId());
      List<VariableSubDocumentProjection> variables = variableRepository
          .findSubDocumentsByRelatedQuestionsInstrumentId(instrument.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository.findSudDocumentsByInstrumentIdsContaining(
              instrument.getId());
      InstrumentSearchDocument searchDocument = new InstrumentSearchDocument(instrument, 
          study, surveys, questions, variables, relatedPublications);
      
      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
              .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  private void addUpsertActionForRelatedPublication(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {

    RelatedPublication relatedPublication =
        relatedPublicationRepository.findOne(lockedItem.getDocumentId());
    if (relatedPublication != null) {
      List<StudySubDocumentProjection> studies = new ArrayList<StudySubDocumentProjection>();
      if (relatedPublication.getStudyIds() != null) {
        studies = studyRepository.findSubDocumentsByIdIn(relatedPublication.getStudyIds());
      }
      List<QuestionSubDocumentProjection> questions = 
          new ArrayList<QuestionSubDocumentProjection>();
      if (relatedPublication.getQuestionIds() != null) {
        questions = questionRepository.findSubDocumentsByIdIn(relatedPublication.getQuestionIds());
      }
      List<InstrumentSubDocumentProjection> instruments = 
          new ArrayList<InstrumentSubDocumentProjection>();
      if (relatedPublication.getInstrumentIds() != null) {
        instruments = instrumentRepository
            .findSubDocumentsByIdIn(relatedPublication.getInstrumentIds());
      }
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (relatedPublication.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(relatedPublication.getSurveyIds());
      }
      List<DataSetSubDocumentProjection> dataSets = new ArrayList<DataSetSubDocumentProjection>();
      if (relatedPublication.getDataSetIds() != null) {
        dataSets = dataSetRepository.findSubDocumentsByIdIn(relatedPublication.getDataSetIds());
      }
      List<VariableSubDocumentProjection> variables = 
          new ArrayList<VariableSubDocumentProjection>();
      if (relatedPublication.getVariableIds() != null) {
        variables = variableRepository.findSubDocumentsByIdIn(relatedPublication.getVariableIds());
      }
      RelatedPublicationSearchDocument searchDocument =
          new RelatedPublicationSearchDocument(relatedPublication, studies, questions,
              instruments, surveys, dataSets, variables);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
            .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  private void addUpsertActionForDataSet(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    DataSet dataSet = dataSetRepository.findOne(lockedItem.getDocumentId());

    if (dataSet != null) {
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(dataSet.getStudyId());
      List<VariableSubDocumentProjection> variables = variableRepository
          .findSubDocumentsByDataSetId(dataSet.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByDataSetIdsContaining(dataSet.getId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (dataSet.getSurveyIds() != null) {        
        surveys = surveyRepository.findSubDocumentByIdIn(dataSet.getSurveyIds());
      }
      DataSetSearchDocument searchDocument = new DataSetSearchDocument(dataSet, study,
          variables, relatedPublications, surveys);
      
      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
              .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  private void addUpsertActionForSurvey(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Survey survey = surveyRepository.findOne(lockedItem.getDocumentId());
    if (survey != null) {
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(survey.getStudyId());        
      List<DataSetSubDocumentProjection> dataSets = dataSetRepository
          .findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<VariableSubDocumentProjection> variables = variableRepository
          .findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<InstrumentSubDocumentProjection> instruments = instrumentRepository
          .findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<String> instrumentIds = instruments.stream()
          .map(InstrumentSubDocumentProjection::getId).collect(Collectors.toList());
      List<QuestionSubDocumentProjection> questions = questionRepository
          .findSubDocumentsByInstrumentIdIn(instrumentIds);
      SurveySearchDocument searchDocument =
           new SurveySearchDocument(survey, study, 
               dataSets, variables, relatedPublications, instruments, questions);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType().name())
          .id(searchDocument.getId())
          .build());
    }
  }

  /**
   * This method creates for the variable repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private void addUpsertActionForVariable(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Variable variable = variableRepository.findOne(lockedItem.getDocumentId());
    if (variable != null) {
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(variable.getStudyId());
      DataSetSubDocumentProjection dataSet = dataSetRepository
          .findOneSubDocumentById(variable.getDataSetId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByVariableIdsContaining(variable.getId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (variable.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(variable.getSurveyIds());        
      }
      List<InstrumentSubDocumentProjection> instruments = new ArrayList<>();
      if (variable.getRelatedQuestions() != null) {
        List<String> instrumentIds = variable.getRelatedQuestions().stream()
            .map(RelatedQuestion::getInstrumentId).collect(Collectors.toList());
        instruments = instrumentRepository.findSubDocumentsByIdIn(instrumentIds);
      }
      VariableSearchDocument searchDocument = new VariableSearchDocument(variable,
          dataSet, study, relatedPublications, surveys, instruments);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
              .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  /**
   * This method creates for the question repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private void addUpsertActionForQuestion(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Question question = questionRepository.findOne(lockedItem.getDocumentId());
    if (question != null) {
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(question.getStudyId());
      InstrumentSubDocumentProjection instrument = instrumentRepository
          .findOneSubDocumentById(question.getInstrumentId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (instrument != null && instrument.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(instrument.getSurveyIds());        
      }
      List<VariableSubDocumentProjection> variables = variableRepository
          .findSubDocumentsByRelatedQuestionsQuestionId(question.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByQuestionIdsContaining(question.getId());
      QuestionSearchDocument searchDocument =
            new QuestionSearchDocument(question, study, instrument, surveys, variables,
                relatedPublications);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
            .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  /**
   * This method creates for the study repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private void addUpsertActionForStudy(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Study study = this.studyRepository.findOne(lockedItem.getDocumentId());
    if (study != null) {
      List<DataSetSubDocumentProjection> dataSets = dataSetRepository
          .findSubDocumentsByStudyId(study.getId());
      List<VariableSubDocumentProjection> variables = variableRepository
          .findSubDocumentsByStudyId(study.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByStudyIdsContaining(study.getId());
      List<SurveySubDocumentProjection> surveys = surveyRepository
          .findSubDocumentByStudyId(study.getId());
      List<QuestionSubDocumentProjection> questions = questionRepository
          .findSubDocumentsByStudyId(study.getId());
      List<InstrumentSubDocumentProjection> instruments = instrumentRepository
          .findSubDocumentsByStudyId(study.getId());
      StudySearchDocument searchDocument = new StudySearchDocument(study, dataSets, variables,
          relatedPublications, surveys, questions, instruments);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
              .name())
          .id(searchDocument.getId())
          .build());
    }
  }
}
