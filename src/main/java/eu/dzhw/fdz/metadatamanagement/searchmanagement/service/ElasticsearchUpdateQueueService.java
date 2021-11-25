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
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.projection.AnalysisPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.repository.AnalysisPackageRepository;
import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.projections.ConceptSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.projection.DataPackageSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
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
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.AnalysisPackageNestedDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.AnalysisPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.AnalysisPackageSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.ConceptSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageNestedDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSubDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataSetSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.InstrumentSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.QuestionSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.RelatedPublicationSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.SurveySearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.VariableSearchDocument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueItem;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.projections.SurveySubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ElasticsearchUpdateQueueService {
  // id used to synchronize multiple jvm instances
  private String jvmId = ManagementFactory.getRuntimeMXBean().getName();

  /**
   * MongoDB Repository with gets all queue elements for the synchronizations of the Elasticsearch
   * DB.
   */
  private final ElasticsearchUpdateQueueItemRepository queueItemRepository;

  /**
   * Repository for the variables for updating them.
   */
  private final VariableRepository variableRepository;

  private final DataSetRepository dataSetRepository;

  /**
   * Repository for the surveys for updating them.
   */
  private final SurveyRepository surveyRepository;

  private final QuestionRepository questionRepository;

  private final DataPackageRepository dataPackageRepository;

  private final RelatedPublicationRepository relatedPublicationRepository;

  private final InstrumentRepository instrumentRepository;

  private final ConceptRepository conceptRepository;

  private final AnalysisPackageRepository analysisPackageRepository;

  private final ElasticsearchDao elasticsearchDao;

  private final Gson gson;

  private final DataAcquisitionProjectRepository projectRepository;

  private final DoiBuilder doiBuilder;

  private final DataAcquisitionProjectVersionsService dataAcquisitionProjectVersionsService;

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
    log.debug("Starting processing of ElasticsearchUpdateQueue...");
    LocalDateTime updateStart = LocalDateTime.now();

    queueItemRepository.lockAllUnlockedOrExpiredItems(updateStart, jvmId);

    List<ElasticsearchUpdateQueueItem> lockedItems =
        queueItemRepository.findOldestLockedItems(jvmId, updateStart);

    boolean refreshIndices = false;
    while (!lockedItems.isEmpty()) {
      refreshIndices = true;
      executeQueueItemActions(lockedItems);

      // check if there are more locked items to process
      lockedItems = queueItemRepository.findOldestLockedItems(jvmId, updateStart);
    }
    if (refreshIndices) {
      elasticsearchDao.refresh(ElasticsearchType.names());
    }
    log.debug("Finished processing of ElasticsearchUpdateQueue...");
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
    BulkRequest request = new BulkRequest();
    boolean bulkNotEmpty = false;
    for (ElasticsearchUpdateQueueItem lockedItem : lockedItems) {
      switch (lockedItem.getAction()) {
        case DELETE:
          bulkNotEmpty = addDeleteAction(lockedItem, request) || bulkNotEmpty;
          break;
        case UPSERT:
          bulkNotEmpty = addUpsertAction(lockedItem, request) || bulkNotEmpty;
          break;
        default:
          throw new NotImplementedException("Processing queue item with action "
              + lockedItem.getAction() + " has not been implemented!");
      }
    }

    // execute the bulk update/delete
    try {
      if (bulkNotEmpty) {
        elasticsearchDao.executeBulk(request);
      }
      // finally delete the queue items
      lockedItems.forEach(item -> queueItemRepository.deleteById(item.getId()));
    } catch (ElasticsearchBulkOperationException ex) {
      log.error("Some documents in Elasticsearch could not be updated!", ex);
    }
  }

  /**
   * Adds a Deletes Action to the bulk builder.
   * 
   * @param lockedItem a locked item.
   * @param request for building a delete action.
   * @return true if an action has been added to the request
   */
  private boolean addDeleteAction(ElasticsearchUpdateQueueItem lockedItem, BulkRequest request) {
    request.add(new DeleteRequest(lockedItem.getDocumentType().name(), lockedItem.getDocumentId()));
    return true;
  }

  /**
   * Add a update / insert action to the bulk builder.
   * 
   * @param lockedItem A locked item.
   * @param request The bulk builder for building update / insert actions.
   * @return true if an action has been added to the request
   */
  private boolean addUpsertAction(ElasticsearchUpdateQueueItem lockedItem, BulkRequest request) {
    switch (lockedItem.getDocumentType()) {
      case variables:
        return addUpsertActionForVariable(lockedItem, request);
      case surveys:
        return addUpsertActionForSurvey(lockedItem, request);
      case data_sets:
        return addUpsertActionForDataSet(lockedItem, request);
      case questions:
        return addUpsertActionForQuestion(lockedItem, request);
      case data_packages:
        return addUpsertActionForDataPackage(lockedItem, request);
      case related_publications:
        return addUpsertActionForRelatedPublication(lockedItem, request);
      case instruments:
        return addUpsertActionForInstrument(lockedItem, request);
      case concepts:
        return addUpsertActionForConcept(lockedItem, request);
      case analysis_packages:
        return addUpsertActionForAnalysisPackage(lockedItem, request);
      default:
        throw new NotImplementedException("Processing queue item with type "
            + lockedItem.getDocumentType() + " has not been implemented!");
    }
  }

  /**
   * This method creates for the analysis packages update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param request A bulk builder for building the actions.
   */
  private boolean addUpsertActionForAnalysisPackage(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    AnalysisPackage analysisPackage =
        this.analysisPackageRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (analysisPackage != null) {
      DataAcquisitionProject project =
          projectRepository.findById(analysisPackage.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByAnalysisPackageIdsContaining(analysisPackage.getMasterId());
      List<DataPackageSubDocumentProjection> dataPackages = null;
      if (analysisPackage.getAnalysisDataPackages() != null) {
        Class<eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage> clazz =
            eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage.class;
        Set<String> dataPackageIds =
            analysisPackage.getAnalysisDataPackages().stream().filter(clazz::isInstance)
                .map(clazz::cast).map(dataPackage -> dataPackage.getDataPackageMasterId() + "-"
                    + dataPackage.getVersion())
                .collect(Collectors.toSet());
        dataPackages = dataPackageRepository.findSubDocumentsByIdIn(dataPackageIds);
      }
      AnalysisPackageSearchDocument searchDocument = new AnalysisPackageSearchDocument(
          analysisPackage, release, configuration, doi, dataPackages, relatedPublications);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForConcept(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    Concept concept = conceptRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (concept != null) {
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByConceptIdsContaining(concept.getId());

      Set<String> instrumentIds = questions.stream().map(question -> question.getInstrumentId())
          .collect(Collectors.toSet());

      instrumentIds.addAll(instrumentRepository.findIdsByConceptIdsContaining(concept.getId())
          .stream().map(IdAndVersionProjection::getId).collect(Collectors.toSet()));

      List<InstrumentSubDocumentProjection> instruments =
          new ArrayList<>(instrumentRepository.findSubDocumentsByIdIn(instrumentIds));

      Set<String> surveyIds = instruments.stream().map(instrument -> instrument.getSurveyIds())
          .flatMap(List::stream).collect(Collectors.toSet());
      List<SurveySubDocumentProjection> surveys = surveyRepository.findSubDocumentByIdIn(surveyIds);

      Set<String> dataPackageIds = instruments.stream()
          .map(instrument -> instrument.getDataPackageId()).collect(Collectors.toSet());
      List<DataPackageSubDocumentProjection> dataPackages =
          dataPackageRepository.findSubDocumentsByIdIn(dataPackageIds);
      List<DataPackageSubDocument> dataPackageSubDocuments =
          dataPackages.stream().map(dataPackage -> {
            DataAcquisitionProject project =
                projectRepository.findById(dataPackage.getDataAcquisitionProjectId()).orElse(null);
            if (project == null) {
              // project has been deleted, skip upsert
              return null;
            }
            return new DataPackageSubDocument(dataPackage,
                doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), getRelease(project)));
          }).filter(document -> document != null).collect(Collectors.toList());
      List<DataPackageNestedDocument> nestedDataPackageDocuments =
          dataPackages.stream().map(DataPackageNestedDocument::new).collect(Collectors.toList());

      Set<String> questionIds =
          questions.stream().map(question -> question.getId()).collect(Collectors.toSet());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByRelatedQuestionsQuestionIdIn(questionIds);

      Set<String> dataSetIds = variables.stream().map(VariableSubDocumentProjection::getDataSetId)
          .collect(Collectors.toSet());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByIdIn(dataSetIds);

      ConceptSearchDocument searchDocument =
          new ConceptSearchDocument(concept, dataPackageSubDocuments, nestedDataPackageDocuments,
              questions, instruments, surveys, dataSets, variables);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForInstrument(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    Instrument instrument = instrumentRepository.findById(lockedItem.getDocumentId()).orElse(null);

    if (instrument != null) {
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
      DataAcquisitionProject project =
          projectRepository.findById(instrument.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      Configuration configuration = project.getConfiguration();
      DataPackageSubDocumentProjection dataPackage =
          dataPackageRepository.findOneSubDocumentById(instrument.getDataPackageId());
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      InstrumentSearchDocument searchDocument =
          new InstrumentSearchDocument(instrument, dataPackage, surveys, questions, variables,
              dataSets, concepts, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForRelatedPublication(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {

    RelatedPublication relatedPublication =
        relatedPublicationRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (relatedPublication != null) {
      List<DataPackageSubDocument> dataPackageSubDocuments = null;
      List<DataPackageNestedDocument> nestedDataPackageDocuments = null;
      if (relatedPublication.getDataPackageIds() != null) {
        List<DataPackageSubDocumentProjection> dataPackages =
            dataPackageRepository.findSubDocumentsByIdIn(relatedPublication.getDataPackageIds());
        dataPackageSubDocuments = dataPackages.stream().map(dataPackage -> {
          DataAcquisitionProject project =
              projectRepository.findById(dataPackage.getDataAcquisitionProjectId()).orElse(null);
          if (project == null) {
            // project has been deleted, skip upsert
            return null;
          }
          return new DataPackageSubDocument(dataPackage,
              doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), getRelease(project)));
        }).filter(document -> document != null).collect(Collectors.toList());
        nestedDataPackageDocuments =
            dataPackages.stream().map(DataPackageNestedDocument::new).collect(Collectors.toList());
      }
      List<AnalysisPackageSubDocument> analysisPackageSubDocuments = null;
      List<AnalysisPackageNestedDocument> nestedAnalysisPackageDocuments = null;
      if (relatedPublication.getAnalysisPackageIds() != null) {
        List<AnalysisPackageSubDocumentProjection> analysisPackages = analysisPackageRepository
            .findSubDocumentsByIdIn(relatedPublication.getAnalysisPackageIds());
        analysisPackageSubDocuments = analysisPackages.stream().map(analysisPackage -> {
          DataAcquisitionProject project = projectRepository
              .findById(analysisPackage.getDataAcquisitionProjectId()).orElse(null);
          if (project == null) {
            // project has been deleted, skip upsert
            return null;
          }
          return new AnalysisPackageSubDocument(analysisPackage,
              doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), getRelease(project)));
        }).filter(document -> document != null).collect(Collectors.toList());
        nestedAnalysisPackageDocuments = analysisPackages.stream()
            .map(AnalysisPackageNestedDocument::new).collect(Collectors.toList());
      }
      RelatedPublicationSearchDocument searchDocument = new RelatedPublicationSearchDocument(
          relatedPublication, dataPackageSubDocuments, nestedDataPackageDocuments,
          analysisPackageSubDocuments, nestedAnalysisPackageDocuments);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForDataSet(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
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
      DataPackageSubDocumentProjection dataPackage =
          dataPackageRepository.findOneSubDocumentById(dataSet.getDataPackageId());
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      DataSetSearchDocument searchDocument =
          new DataSetSearchDocument(dataSet, dataPackage, variableProjections, surveys, instruments,
              questions, concepts, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  private boolean addUpsertActionForSurvey(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    Survey survey = surveyRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (survey != null) {
      DataPackageSubDocumentProjection dataPackage =
          dataPackageRepository.findOneSubDocumentById(survey.getDataPackageId());
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
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
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      SurveySearchDocument searchDocument = new SurveySearchDocument(survey, dataPackage, dataSets,
          variables, instruments, questions, concepts, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
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
   * @param request A bulk builder for building the actions.
   */
  private boolean addUpsertActionForVariable(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    Variable variable = variableRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (variable != null) {
      final DataPackageSubDocumentProjection dataPackage =
          dataPackageRepository.findOneSubDocumentById(variable.getDataPackageId());
      final DataSetSubDocumentProjection dataSet =
          dataSetRepository.findOneSubDocumentById(variable.getDataSetId());
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
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      VariableSearchDocument searchDocument = new VariableSearchDocument(variable, dataSet,
          dataPackage, surveys, instruments, questions, concepts, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  /**
   * This method creates for the question repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param request A bulk builder for building the actions.
   */
  private boolean addUpsertActionForQuestion(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    Question question = questionRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (question != null) {
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
      DataPackageSubDocumentProjection dataPackage =
          dataPackageRepository.findOneSubDocumentById(question.getDataPackageId());
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      QuestionSearchDocument searchDocument = new QuestionSearchDocument(question, dataPackage,
          instrument, surveys, variables, dataSets, concepts, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
      return true;
    }
    return false;
  }

  /**
   * This method creates for the dataPackage repository update / insert actions.
   * 
   * @param lockedItem A locked item.
   * @param request A bulk builder for building the actions.
   */
  private boolean addUpsertActionForDataPackage(ElasticsearchUpdateQueueItem lockedItem,
      BulkRequest request) {
    DataPackage dataPackage =
        this.dataPackageRepository.findById(lockedItem.getDocumentId()).orElse(null);
    if (dataPackage != null) {
      List<DataSetSubDocumentProjection> dataSets =
          dataSetRepository.findSubDocumentsByDataPackageId(dataPackage.getId());
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByDataPackageId(dataPackage.getId());
      List<RelatedPublicationSubDocumentProjection> relatedPublications =
          relatedPublicationRepository
              .findSubDocumentsByDataPackageIdsContaining(dataPackage.getMasterId());
      List<SurveySubDocumentProjection> surveys =
          surveyRepository.findSubDocumentByDataPackageId(dataPackage.getId());
      List<QuestionSubDocumentProjection> questions =
          questionRepository.findSubDocumentsByDataPackageId(dataPackage.getId());
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsByDataPackageId(dataPackage.getId());
      List<ConceptSubDocumentProjection> concepts = getConcepts(instruments, questions);
      DataAcquisitionProject project =
          projectRepository.findById(dataPackage.getDataAcquisitionProjectId()).orElse(null);
      if (project == null) {
        // project has been deleted, skip upsert
        return false;
      }
      Release release = getRelease(project);
      List<AnalysisPackageSubDocumentProjection> analysisPackages = null;
      if (dataPackage.isShadow() && release != null) {
        analysisPackages = analysisPackageRepository
            .findByDataPackageMasterIdAndVersion(dataPackage.getMasterId(), release.getVersion());
      }
      Configuration configuration = project.getConfiguration();
      String doi = doiBuilder.buildDataOrAnalysisPackageDoi(project.getId(), release);
      DataPackageSearchDocument searchDocument = new DataPackageSearchDocument(dataPackage,
          dataSets, variables, relatedPublications, surveys, questions, instruments, concepts,
          analysisPackages, release, doi, configuration);

      request.add(new IndexRequest(lockedItem.getDocumentType().name()).id(searchDocument.getId())
          .source(gson.toJson(searchDocument), XContentType.JSON));
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
    elasticsearchDao.refresh(type.name());
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
