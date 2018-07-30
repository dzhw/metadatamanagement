package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchBulkOperationException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedPublicationSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSubDocument;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Service which manages asynchronous Elasticsearch updates as a FIFO queue. Inserting an item into
 * the queue which already exists will remove the existing one and insert the new item at the end of
 * the queue.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@Slf4j
public class ElasticsearchUpdateQueueService {
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
  
  @Autowired
  private DataAcquisitionProjectRepository projectRepository;
  
  @Autowired
  private DoiBuilder doiBuilder; 

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
        queueItemRepository.deleteById(existingItem.getId());
      }
      queueItemRepository
        .insert(new ElasticsearchUpdateQueueItem(documentId, documentType, action));
    } catch (DuplicateKeyException ex) {
      log.debug("Ignoring attempt to enqueue a duplicate action.");
    }
  }

  /**
   * Process the update queue every minute.
   */
  @Scheduled(fixedRate = 1000 * 60, initialDelay = 1000 * 60)
  public void processAllQueueItems() {
    log.info("Starting processing of ElasticsearchUpdateQueue...");
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItems(updateStart, jvmId);

    List<ElasticsearchUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItems(jvmId, updateStart);

    while (!lockedItems.isEmpty()) {
      executeQueueItemActions(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItems(jvmId, updateStart);
    }
    log.info("Finished processing of ElasticsearchUpdateQueue...");
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
    try {
      elasticsearchDao.executeBulk(bulkBuilder.build());      
    } catch (ElasticsearchBulkOperationException ex) {
      log.error("Some documents in Elasticsearch could not be updated!", ex);
    }

    // finally delete the queue items
    queueItemRepository.deleteAll(lockedItems);
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
    Instrument instrument = instrumentRepository.findById(lockedItem.getDocumentId())
        .orElse(null);

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
      Set<String> dataSetIds = variables.stream()
          .map(variable -> variable.getDataSetId())
          .collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets = dataSetRepository
          .findSubDocumentsByIdIn(dataSetIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository.findSubDocumentsByInstrumentIdsContaining(
              instrument.getId());
      DataAcquisitionProject project = projectRepository.findById(
          instrument.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      String doi = doiBuilder.buildStudyDoi(study, release);
      InstrumentSearchDocument searchDocument = new InstrumentSearchDocument(instrument, 
          study, surveys, questions, variables, dataSets, relatedPublications, release, doi);
      
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
        relatedPublicationRepository.findById(lockedItem.getDocumentId())
        .orElse(null);
    if (relatedPublication != null) {
      List<StudySubDocument> studySubDocuments = null;
      if (relatedPublication.getStudyIds() != null) {
        List<StudySubDocumentProjection> studies = studyRepository
            .findSubDocumentsByIdIn(relatedPublication.getStudyIds());
        studySubDocuments = studies.stream().map(study -> {
          DataAcquisitionProject project = projectRepository.findById(study
              .getDataAcquisitionProjectId()).get();
          return new StudySubDocument(study, 
              doiBuilder.buildStudyDoi(study, project.getRelease()));
        }).collect(Collectors.toList());
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
          new RelatedPublicationSearchDocument(relatedPublication, studySubDocuments, questions,
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
    DataSet dataSet = dataSetRepository.findById(lockedItem.getDocumentId()).orElse(null);

    if (dataSet != null) {
      List<Variable> variables = variableRepository
          .findByDataSetId(dataSet.getId());
      List<VariableSubDocumentProjection> variableProjections = new ArrayList<>(variables.size());
      Set<String> questionIds = new HashSet<>();
      Set<String> instrumentIds = new HashSet<>();
      variables.forEach(variable -> {
        variableProjections.add(new VariableSubDocument(variable));
        if (variable.getRelatedQuestions() != null) {
          variable.getRelatedQuestions().forEach(relatedQuestion -> {
            questionIds.add(relatedQuestion.getQuestionId());
            instrumentIds.add(relatedQuestion.getInstrumentId());
          });
        }
      });
      List<InstrumentSubDocumentProjection> instruments = instrumentRepository
          .findSubDocumentsByIdIn(instrumentIds);
      List<QuestionSubDocumentProjection> questions = questionRepository
          .findSubDocumentsByIdIn(questionIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByDataSetIdsContaining(dataSet.getId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (dataSet.getSurveyIds() != null) {        
        surveys = surveyRepository.findSubDocumentByIdIn(dataSet.getSurveyIds());
      }
      DataAcquisitionProject project = projectRepository.findById(
          dataSet.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(dataSet.getStudyId());
      String doi = doiBuilder.buildStudyDoi(study, release);
      DataSetSearchDocument searchDocument = new DataSetSearchDocument(dataSet, study,
          variableProjections, relatedPublications, surveys, instruments, questions, release, doi);
      
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
    Survey survey = surveyRepository.findById(lockedItem.getDocumentId()).orElse(null);
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
      DataAcquisitionProject project = projectRepository.findById(
          survey.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      String doi = doiBuilder.buildStudyDoi(study, release);
      SurveySearchDocument searchDocument =
           new SurveySearchDocument(survey, study, 
               dataSets, variables, relatedPublications, instruments, questions, release, doi);

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
    Variable variable = variableRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (variable != null) {
      final StudySubDocumentProjection study = studyRepository
          .findOneSubDocumentById(variable.getStudyId());
      final DataSetSubDocumentProjection dataSet = dataSetRepository
          .findOneSubDocumentById(variable.getDataSetId());
      final List<RelatedPublicationSubDocumentProjection> relatedPublications = 
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
      DataAcquisitionProject project = projectRepository.findById(
          variable.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      String doi = doiBuilder.buildStudyDoi(study, release);
      VariableSearchDocument searchDocument = new VariableSearchDocument(variable,
          dataSet, study, relatedPublications, surveys, instruments, release, doi);

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
    Question question = questionRepository.findById(lockedItem.getDocumentId()).orElse(null);
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
      Set<String> dataSetIds = variables.stream()
          .map(variable -> variable.getDataSetId())
          .collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets = dataSetRepository
          .findSubDocumentsByIdIn(dataSetIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications = 
          relatedPublicationRepository
            .findSubDocumentsByQuestionIdsContaining(question.getId());
      DataAcquisitionProject project = projectRepository.findById(
          question.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      String doi = doiBuilder.buildStudyDoi(study, release);
      QuestionSearchDocument searchDocument =
            new QuestionSearchDocument(question, study, instrument, surveys, variables,
                dataSets, relatedPublications, release, doi);

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
    Study study = this.studyRepository.findById(lockedItem.getDocumentId()).orElse(null);
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
      List<RelatedPublicationSubDocumentProjection> seriesPublications = null;
      if (study.getStudySeries() != null) {
        seriesPublications = relatedPublicationRepository
            .findSubDocumentsByStudySeriesesContaining(study.getStudySeries()); 
      }
      DataAcquisitionProject project = projectRepository.findById(
          study.getDataAcquisitionProjectId()).orElse(null);
      Release release = null;
      if (project != null) {
        release = project.getRelease();
      }
      String doi = doiBuilder.buildStudyDoi(study, release);
      StudySearchDocument searchDocument = new StudySearchDocument(study, dataSets, variables,
          relatedPublications, surveys, questions, instruments, seriesPublications, release, doi);

      bulkBuilder.addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType()
          .name())
          .type(lockedItem.getDocumentType()
              .name())
          .id(searchDocument.getId())
          .build());
    }
  }

  /**
   * Process only the update queue items of the given type.
   * @param type the type of items to be processed.
   */
  public void processQueueItems(ElasticsearchType type) {
    log.info("Starting processing of ElasticsearchUpdateQueue for type: " + type.name());
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItemsByType(updateStart, jvmId, type);

    List<ElasticsearchUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItemsByType(jvmId, updateStart, type);

    while (!lockedItems.isEmpty()) {
      executeQueueItemActions(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItemsByType(jvmId, updateStart, type);
    }
    log.info("Finished processing of ElasticsearchUpdateQueue for type: " + type.name());
  }
  
  /**
   * Asynchronously attach the given documents to the update queue.
   * 
   * @param streamProvider A closure returning a stream of {@link IdAndVersionProjection}s
   * @param type The {@link ElasticsearchType} of the documents.
   */
  @Async
  public void enqueueUpsertsAsync(IdStreamProvider streamProvider, 
      ElasticsearchType type) {
    Stream<IdAndVersionProjection> idStream = streamProvider.get();
    enqueueStreamUpserts(type, idStream);
  }

  
  /**
   * Asynchronously attach the given documents to the update queue.
   * 
   * @param streamsProvider A closure returning a list of streams of {@link IdAndVersionProjection}s
   * @param type The {@link ElasticsearchType} of the documents.
   */
  @Async
  public void enqueueUpsertsAsync(MultipleIdStreamsProvider streamsProvider, 
      ElasticsearchType type) {
    List<Stream<IdAndVersionProjection>> streams = streamsProvider.get();
    if (streams != null) {
      for (Stream<IdAndVersionProjection> stream : streams) {
        enqueueStreamUpserts(type, stream);
      }
    }
  }
  
  private void enqueueStreamUpserts(ElasticsearchType type,
      Stream<IdAndVersionProjection> idStream) {
    if (idStream != null) {
      try (Stream<IdAndVersionProjection> stream = idStream) {
        stream.forEach(document -> {
          this.enqueue(document.getId(),
              type, ElasticsearchUpdateQueueAction.UPSERT);
        });      
      }      
    }
  }
  
  /**
   * Asynchronously attach the given document to the update queue.
   * 
   * @param idProvider A closure returning a {@link IdAndVersionProjection}
   * @param type The {@link ElasticsearchType} of the document.
   */
  @Async
  public void enqueueUpsertAsync(IdProvider idProvider, ElasticsearchType type) {
    IdAndVersionProjection document = idProvider.get();
    if (document != null) {      
      this.enqueue(document.getId(),
          type, ElasticsearchUpdateQueueAction.UPSERT);
    }
  }
}
 