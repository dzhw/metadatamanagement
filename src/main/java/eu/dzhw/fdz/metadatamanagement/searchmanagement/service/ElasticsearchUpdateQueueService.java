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
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections.DataSetSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Configuration;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.repository.DataAcquisitionProjectRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.helper.DoiBuilder;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.repository.RelatedPublicationRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.ElasticsearchDao;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.dao.exception.ElasticsearchBulkOperationException;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ConceptSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedPublicationSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudyNestedDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.StudySubDocument;
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
  private ConceptRepository conceptRepository;

  @Autowired
  private ElasticsearchDao elasticsearchDao;

  @Autowired
  private DataAcquisitionProjectRepository projectRepository;

  @Autowired
  private DoiBuilder doiBuilder;
  
  @Autowired
  private DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

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
    boolean bulkNotEmpty = false;
    for (ElasticsearchUpdateQueueItem lockedItem : lockedItems) {
      switch (lockedItem.getAction()) {
        case DELETE:
          bulkNotEmpty = addDeleteAction(lockedItem, bulkBuilder) || bulkNotEmpty;
          break;
        case UPSERT:
          bulkNotEmpty = addUpsertAction(lockedItem, bulkBuilder) || bulkNotEmpty;
          break;
        default:
          throw new NotImplementedException("Processing queue item with action "
              + lockedItem.getAction() + " has not been implemented!");
      }
    }

    // execute the bulk update/delete
    try {
      if (bulkNotEmpty) {        
        elasticsearchDao.executeBulk(bulkBuilder.build());
      }
      // finally delete the queue items
      queueItemRepository.deleteAll(lockedItems);
    } catch (ElasticsearchBulkOperationException ex) {
      log.error("Some documents in Elasticsearch could not be updated!", ex);
    }
  }

  /**
   * Adds a Deletes Action to the bulk builder.
   * 
   * @param lockedItem a locked item.
   * @param bulkBuilder for building an add action.
   * @return true if an action has been added to the bulkBuilder
   */
  private boolean addDeleteAction(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    bulkBuilder.addAction(
        new Delete.Builder(lockedItem.getDocumentId())
                  .index(lockedItem.getDocumentType().name())
                  .type(lockedItem.getDocumentType().name())
                  .build());
    return true;
  }

  /**
   * Add a update / insert action to the bulk builder.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder The bulk builder for building update / insert actions.
   * @return true if an action has been added to the bulkBuilder
   */
  private boolean addUpsertAction(ElasticsearchUpdateQueueItem lockedItem, Builder bulkBuilder) {
    switch (lockedItem.getDocumentType()) {
      case variables:
        return addUpsertActionForVariable(lockedItem, bulkBuilder);
      case surveys:
        return addUpsertActionForSurvey(lockedItem, bulkBuilder);
      case data_sets:
        return addUpsertActionForDataSet(lockedItem, bulkBuilder);
      case questions:
        return addUpsertActionForQuestion(lockedItem, bulkBuilder);
      case studies:
        return addUpsertActionForStudy(lockedItem, bulkBuilder);
      case related_publications:
        return addUpsertActionForRelatedPublication(lockedItem, bulkBuilder);
      case instruments:
        return addUpsertActionForInstrument(lockedItem, bulkBuilder);
      case concepts:
        return addUpsertActionForConcept(lockedItem, bulkBuilder);
      default:
        throw new NotImplementedException("Processing queue item with type "
            + lockedItem.getDocumentType() + " has not been implemented!");
    }
  }

  private boolean addUpsertActionForConcept(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Concept concept = conceptRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (concept != null) {
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByConceptIdsContaining(concept.getId());

      Set<String> instrumentIds = questions.stream().map(question -> question.getInstrumentId())
          .collect(Collectors.toSet());

      instrumentIds.addAll(instrumentRepository.findIdsByConceptIdsContaining(concept.getId())
          .stream().map(IdAndVersionProjection::getId).collect(Collectors.toSet()));

      List<InstrumentSubDocumentProjection> instruments = new ArrayList<>(instrumentRepository
          .findSubDocumentsByIdIn(instrumentIds));

      Set<String> surveyIds = instruments.stream().map(instrument -> instrument.getSurveyIds())
          .flatMap(List::stream).collect(Collectors.toSet());
      List<SurveySubDocumentProjection> surveys = surveyRepository.findSubDocumentByIdIn(surveyIds);

      Set<String> studyIds = instruments.stream().map(instrument -> instrument.getStudyId())
          .collect(Collectors.toSet());
      List<StudySubDocumentProjection> studies = studyRepository.findSubDocumentsByIdIn(studyIds);
      List<StudySubDocument> studySubDocuments = studies.stream().map(study -> {
        DataAcquisitionProject project =
            projectRepository.findById(study.getDataAcquisitionProjectId()).orElse(null);
        if (project == null) {
          // project has been deleted, skip upsert
          return null;
        }
        return new StudySubDocument(study, doiBuilder.buildStudyDoi(study, getRelease(project)));
      }).filter(document -> document != null).collect(Collectors.toList());
      List<StudyNestedDocument> nestedStudyDocuments =
          studies.stream().map(StudyNestedDocument::new).collect(Collectors.toList());

      Set<String> questionIds =
          questions.stream().map(question -> question.getId()).collect(Collectors.toSet());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByRelatedQuestionsQuestionIdIn(questionIds);

      Set<String> dataSetIds = variables.stream().map(VariableSubDocumentProjection::getDataSetId)
          .collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByIdIn(dataSetIds);

      ConceptSearchDocument searchDocument = new ConceptSearchDocument(concept, studySubDocuments,
          nestedStudyDocuments, questions, instruments, surveys, dataSets, variables);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForInstrument(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Instrument instrument = instrumentRepository.findById(lockedItem.getDocumentId()).orElse(null);

    if (instrument != null) {
      StudySubDocumentProjection study =
          studyRepository.findOneSubDocumentById(instrument.getStudyId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (instrument.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(instrument.getSurveyIds());
      }
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByInstrumentId(instrument.getId());
      Set<String> conceptIds = questions.stream()
          .map(question -> question.getConceptIds() != null ? question.getConceptIds()
              : new ArrayList<String>())
          .flatMap(List::stream).collect(Collectors.toSet());
      if (instrument.getConceptIds() != null) {
        conceptIds.addAll(instrument.getConceptIds());
      }
      List<ConceptSubDocumentProjection> concepts =
          conceptRepository.findSubDocumentsByIdIn(conceptIds);
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByRelatedQuestionsInstrumentId(instrument.getId());
      Set<String> dataSetIds =
          variables.stream().map(variable -> variable.getDataSetId()).collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByIdIn(dataSetIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByInstrumentIdsContaining(instrument.getMasterId());
      DataAcquisitionProject project =
          projectRepository.findById(instrument.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildStudyDoi(study, release);
      InstrumentSearchDocument searchDocument =
          new InstrumentSearchDocument(instrument, study, surveys, questions, variables, dataSets,
              relatedPublications, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForRelatedPublication(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {

    RelatedPublication relatedPublication =
        relatedPublicationRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (relatedPublication != null) {
      List<StudySubDocument> studySubDocuments = null;
      List<StudyNestedDocument> nestedStudyDocuments = null;
      if (relatedPublication.getStudyIds() != null) {
        List<StudySubDocumentProjection> studies =
            studyRepository.findSubDocumentsByIdIn(relatedPublication.getStudyIds());
        studySubDocuments = studies.stream().map(study -> {
          DataAcquisitionProject project =
              projectRepository.findById(study.getDataAcquisitionProjectId()).orElse(null);
          if (project == null) {
            // project has been deleted, skip upsert
            return null;
          }
          return new StudySubDocument(study, doiBuilder.buildStudyDoi(study, getRelease(project)));
        }).filter(document -> document != null).collect(Collectors.toList());
        nestedStudyDocuments =
            studies.stream().map(StudyNestedDocument::new).collect(Collectors.toList());
      }
      List<QuestionSubDocumentProjection> questions =
          new ArrayList<QuestionSubDocumentProjection>();
      if (relatedPublication.getQuestionIds() != null) {
        questions = questionRepository.findSubDocumentsByIdIn(relatedPublication.getQuestionIds());
      }
      List<InstrumentSubDocumentProjection> instruments = new ArrayList<>();
      if (relatedPublication.getInstrumentIds() != null) {
        instruments =
            instrumentRepository.findSubDocumentsByIdIn(relatedPublication.getInstrumentIds());
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
          new RelatedPublicationSearchDocument(relatedPublication, studySubDocuments,
              nestedStudyDocuments, questions, instruments, surveys, dataSets, variables);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForDataSet(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    DataSet dataSet = dataSetRepository.findById(lockedItem.getDocumentId()).orElse(null);

    if (dataSet != null) {
      List<VariableSubDocumentProjection> variableProjections =
          variableRepository.findSubDocumentsByDataSetId(dataSet.getId());
      Set<String> questionIds = new HashSet<>();
      Set<String> instrumentIds = new HashSet<>();
      variableProjections.forEach(variable -> {
        if (variable.getRelatedQuestions() != null) {
          variable.getRelatedQuestions().forEach(relatedQuestion -> {
            questionIds.add(relatedQuestion.getQuestionId());
            instrumentIds.add(relatedQuestion.getInstrumentId());
          });
        }
      });
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsByIdIn(instrumentIds);
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByIdIn(questionIds);
      Set<String> conceptIds = questions.stream()
          .map(question -> question.getConceptIds() != null ? question.getConceptIds()
              : new ArrayList<String>())
          .flatMap(List::stream).collect(Collectors.toSet());
      List<ConceptSubDocumentProjection> concepts =
          conceptRepository.findSubDocumentsByIdIn(conceptIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByDataSetIdsContaining(dataSet.getMasterId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (dataSet.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(dataSet.getSurveyIds());
      }
      DataAcquisitionProject project =
          projectRepository.findById(dataSet.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      StudySubDocumentProjection study =
          studyRepository.findOneSubDocumentById(dataSet.getStudyId());
      String doi = doiBuilder.buildStudyDoi(study, release);
      DataSetSearchDocument searchDocument =
          new DataSetSearchDocument(dataSet, study, variableProjections, relatedPublications,
              surveys, instruments, questions, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForSurvey(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Survey survey = surveyRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (survey != null) {
      StudySubDocumentProjection study =
          studyRepository.findOneSubDocumentById(survey.getStudyId());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository.findSubDocumentsBySurveyIdsContaining(survey.getMasterId());
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<String> instrumentIds = instruments.stream().map(InstrumentSubDocumentProjection::getId)
          .collect(Collectors.toList());
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByInstrumentIdIn(instrumentIds);
      List<ConceptSubDocumentProjection> concepts = getConcepts(instruments, questions);
      DataAcquisitionProject project =
          projectRepository.findById(survey.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildStudyDoi(study, release);
      SurveySearchDocument searchDocument =
          new SurveySearchDocument(survey, study, dataSets, variables, relatedPublications,
              instruments, questions, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  private List<ConceptSubDocumentProjection> getConcepts(
      List<InstrumentSubDocumentProjection> instruments,
      List<QuestionSubDocumentProjection> questions) {
    Set<String> conceptIds = instruments.stream()
        .map(instrument -> instrument.getConceptIds() != null ? instrument.getConceptIds()
            : new ArrayList<String>())
        .flatMap(List::stream).collect(Collectors.toSet());
    conceptIds.addAll(questions.stream()
        .map(question -> question.getConceptIds() != null ? question.getConceptIds()
            : new ArrayList<String>())
        .flatMap(List::stream).collect(Collectors.toSet()));
    List<ConceptSubDocumentProjection> concepts =
        conceptRepository.findSubDocumentsByIdIn(conceptIds);
    return concepts;
  }

  /**
   * This method creates for the variable repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private boolean addUpsertActionForVariable(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Variable variable = variableRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (variable != null) {
      final StudySubDocumentProjection study =
          studyRepository.findOneSubDocumentById(variable.getStudyId());
      final DataSetSubDocumentProjection dataSet =
          dataSetRepository.findOneSubDocumentById(variable.getDataSetId());
      final List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByVariableIdsContaining(variable.getMasterId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (variable.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(variable.getSurveyIds());
      }
      List<InstrumentSubDocumentProjection> instruments = new ArrayList<>();
      List<QuestionSubDocumentProjection> questions = new ArrayList<>();
      List<ConceptSubDocumentProjection> concepts = new ArrayList<>();
      if (variable.getRelatedQuestions() != null) {
        Set<String> instrumentIds = variable.getRelatedQuestions().stream()
            .map(RelatedQuestion::getInstrumentId).collect(Collectors.toSet());
        Set<String> questionIds = variable.getRelatedQuestions().stream()
            .map(RelatedQuestion::getQuestionId).collect(Collectors.toSet());
        instruments = instrumentRepository.findSubDocumentsByIdIn(instrumentIds);
        questions = questionRepository.findSubDocumentsByIdIn(questionIds);
        Set<String> conceptIds = questions.stream()
            .map(question -> question.getConceptIds() != null ? question.getConceptIds()
                : new ArrayList<String>())
            .flatMap(List::stream).collect(Collectors.toSet());
        concepts = conceptRepository.findSubDocumentsByIdIn(conceptIds);
      }
      DataAcquisitionProject project =
          projectRepository.findById(variable.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildStudyDoi(study, release);
      VariableSearchDocument searchDocument =
          new VariableSearchDocument(variable, dataSet, study, relatedPublications, surveys,
              instruments, questions, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  /**
   * This method creates for the question repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private boolean addUpsertActionForQuestion(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Question question = questionRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (question != null) {
      StudySubDocumentProjection study =
          studyRepository.findOneSubDocumentById(question.getStudyId());
      InstrumentSubDocumentProjection instrument =
          instrumentRepository.findOneSubDocumentById(question.getInstrumentId());
      List<SurveySubDocumentProjection> surveys = new ArrayList<SurveySubDocumentProjection>();
      if (instrument != null && instrument.getSurveyIds() != null) {
        surveys = surveyRepository.findSubDocumentByIdIn(instrument.getSurveyIds());
      }
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByRelatedQuestionsQuestionId(question.getId());
      Set<String> dataSetIds =
          variables.stream().map(variable -> variable.getDataSetId()).collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByIdIn(dataSetIds);
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByQuestionIdsContaining(question.getMasterId());
      DataAcquisitionProject project =
          projectRepository.findById(question.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      List<ConceptSubDocumentProjection> concepts = new ArrayList<>();
      if (question.getConceptIds() != null) {
        concepts = conceptRepository.findSubDocumentsByIdIn(question.getConceptIds());
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildStudyDoi(study, release);
      QuestionSearchDocument searchDocument =
          new QuestionSearchDocument(question, study, instrument, surveys, variables, dataSets,
              relatedPublications, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  /**
   * This method creates for the study repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param bulkBuilder A bulk builder for building the actions.
   */
  private boolean addUpsertActionForStudy(ElasticsearchUpdateQueueItem lockedItem,
      Builder bulkBuilder) {
    Study study = this.studyRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (study != null) {
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByStudyId(study.getId());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByStudyId(study.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository.findSubDocumentsByStudyIdsContaining(study.getMasterId());
      List<SurveySubDocumentProjection> surveys =
          surveyRepository.findSubDocumentByStudyId(study.getId());
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByStudyId(study.getId());
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsByStudyId(study.getId());
      List<RelatedPublicationSubDocumentProjection> seriesPublications = null;
      if (study.getStudySeries() != null) {
        seriesPublications = relatedPublicationRepository
            .findSubDocumentsByStudySeriesesContaining(study.getStudySeries());
      }
      List<ConceptSubDocumentProjection> concepts = getConcepts(instruments, questions);
      DataAcquisitionProject project =
          projectRepository.findById(study.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildStudyDoi(study, release);
      StudySearchDocument searchDocument =
          new StudySearchDocument(study, dataSets, variables, relatedPublications, surveys,
              questions, instruments, seriesPublications, concepts, release, doi, configuration);

      bulkBuilder
          .addAction(new Index.Builder(searchDocument).index(lockedItem.getDocumentType().name())
              .type(lockedItem.getDocumentType().name()).id(searchDocument.getId()).build());
      return true;
    }
    return false;
  }

  /**
   * Process only the update queue items of the given type.
   * 
   * @param type the type of items to be processed.
   */
  public void processQueueItems(ElasticsearchType type) {
    log.debug("Starting processing of ElasticsearchUpdateQueue for type: " + type.name());
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItemsByType(updateStart, jvmId, type);

    List<ElasticsearchUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItemsByType(jvmId, updateStart, type);

    while (!lockedItems.isEmpty()) {
      executeQueueItemActions(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItemsByType(jvmId, updateStart, type);
    }
    log.debug("Finished processing of ElasticsearchUpdateQueue for type: " + type.name());
  }

  /**
   * Asynchronously attach the given documents to the update queue.
   * 
   * @param streamProvider A closure returning a stream of {@link IdAndVersionProjection}s
   * @param type The {@link ElasticsearchType} of the documents.
   */
  @Async
  public void enqueueUpsertsAsync(IdStreamProvider streamProvider, ElasticsearchType type) {
    Stream<? extends IdAndVersionProjection> idStream = streamProvider.get();
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
    List<Stream<? extends IdAndVersionProjection>> streams = streamsProvider.get();
    if (streams != null) {
      for (Stream<? extends IdAndVersionProjection> stream : streams) {
        enqueueStreamUpserts(type, stream);
      }
    }
  }

  private void enqueueStreamUpserts(ElasticsearchType type,
      Stream<? extends IdAndVersionProjection> idStream) {
    try (Stream<? extends IdAndVersionProjection> stream = idStream) {
      stream.forEach(document -> {
        this.enqueue(document.getId(), type, ElasticsearchUpdateQueueAction.UPSERT);
      });
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
      this.enqueue(document.getId(), type, ElasticsearchUpdateQueueAction.UPSERT);
    }
  }
  
  private Release getRelease(DataAcquisitionProject project) {
    Release release = project.getRelease();
    if (release == null) {
      release = dataAcquisitionProjectVersionsService.findLastRelease(project.getId()); 
    }
    return release;
  }
}
