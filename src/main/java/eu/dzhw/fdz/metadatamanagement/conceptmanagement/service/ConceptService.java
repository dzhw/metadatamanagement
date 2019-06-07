package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Service for creating and updating {@link Concept}s. Used for updating concepts in mongo and
 * elasticsearch.
 */
@Service
@RepositoryEventHandler
public class ConceptService {

  @Autowired
  private ConceptRepository conceptRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private VariableRepository variableRepository;

  @Autowired
  private ConceptAttachmentService conceptAttachmentService;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  /**
   * Enqueue deletion of concept search document when the concept is deleted.
   * 
   * @param concept the deleted {@link Concept}.
   */
  @HandleAfterDelete
  public void onConceptDeleted(Concept concept) {
    conceptAttachmentService.deleteAllByConceptId(concept.getId());
    elasticsearchUpdateQueueService.enqueue(concept.getId(), ElasticsearchType.concepts,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of concept search document when the concept is updated.
   * 
   * @param concept the updated or created {@link Concept}.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onConceptSaved(Concept concept) {
    elasticsearchUpdateQueueService.enqueue(concept.getId(), ElasticsearchType.concepts,
        ElasticsearchUpdateQueueAction.UPSERT);
  }

  /**
   * Enqueue update of concept search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsByStudyId(study.getId());
      return getConceptsForInstruments(instruments);
    }, ElasticsearchType.concepts);
  }

  private Stream<? extends IdAndVersionProjection> getConceptsForInstruments(
      List<InstrumentSubDocumentProjection> instruments) {
    Set<String> conceptIds = new HashSet<>();
    conceptIds.addAll(instruments.stream()
        .map(instrument -> instrument.getConceptIds() != null ? instrument.getConceptIds()
            : new ArrayList<String>())
        .flatMap(List::stream).collect(Collectors.toSet()));

    Set<String> instrumentIds =
        instruments.stream().map(instrument -> instrument.getId()).collect(Collectors.toSet());
    conceptIds.addAll(questionRepository.findSubDocumentsByInstrumentIdIn(instrumentIds).stream()
        .map(question -> question.getConceptIds() != null ? question.getConceptIds()
            : new ArrayList<String>())
        .flatMap(List::stream).collect(Collectors.toSet()));
    return conceptRepository.streamIdsByIdIn(conceptIds);
  }


  /**
   * Enqueue update of concept search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      if (question.getConceptIds() != null) {
        conceptIds.addAll(question.getConceptIds());
      }
      return conceptRepository.streamIdsByIdIn(conceptIds);
    }, ElasticsearchType.concepts);
  }

  /**
   * Enqueue update of concept search documents when the instrument changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      if (instrument.getConceptIds() != null) {
        conceptIds.addAll(instrument.getConceptIds());
      }
      conceptIds.addAll(questionRepository.findSubDocumentsByInstrumentId(instrument.getId())
          .stream().map(question -> question.getConceptIds() != null ? question.getConceptIds()
              : new ArrayList<String>())
          .flatMap(List::stream).collect(Collectors.toSet()));
      return conceptRepository.streamIdsByIdIn(conceptIds);
    }, ElasticsearchType.concepts);
  }

  /**
   * Enqueue update of concept search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsBySurveyIdsContaining(survey.getId());
      return getConceptsForInstruments(instruments);
    }, ElasticsearchType.concepts);
  }

  /**
   * Enqueue update of concept search documents when the data set changed.
   * 
   * @param dataSet the updated, created or deleted data set.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      List<VariableSubDocumentProjection> variables =
          variableRepository.findSubDocumentsByDataSetId(dataSet.getId());
      Set<String> questionIds = variables.stream()
          .map(variable -> variable.getRelatedQuestions() != null ? variable.getRelatedQuestions()
              : new ArrayList<RelatedQuestionSubDocumentProjection>())
          .flatMap(List::stream).map(relatedQuestion -> relatedQuestion.getQuestionId())
          .collect(Collectors.toSet());
      conceptIds.addAll(questionRepository.findSubDocumentsByIdIn(questionIds).stream()
          .map(question -> question.getConceptIds() != null ? question.getConceptIds()
              : new ArrayList<String>())
          .flatMap(List::stream).collect(Collectors.toSet()));
      return conceptRepository.streamIdsByIdIn(conceptIds);
    }, ElasticsearchType.concepts);
  }

  /**
   * Enqueue update of concept search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      Set<String> questionIds = new HashSet<>();
      if (variable.getRelatedQuestions() != null) {
        questionIds.addAll(variable.getRelatedQuestions().stream()
            .map(question -> question.getQuestionId()).collect(Collectors.toSet()));
      }
      conceptIds.addAll(questionRepository.findSubDocumentsByIdIn(questionIds).stream()
          .map(question -> question.getConceptIds() != null ? question.getConceptIds()
              : new ArrayList<String>())
          .flatMap(List::stream).collect(Collectors.toSet()));
      return conceptRepository.streamIdsByIdIn(conceptIds);
    }, ElasticsearchType.concepts);
  }

  /**
   * Deletes a concept by id.
   * @param conceptId Id of concept to delete
   * @throws ConceptInUseException Thrown if concept is referenced in an Instrument or Question
   */
  public void deleteConcept(String conceptId) {
    Set<String> instrumentIds = conceptRepository.findInstrumentIdsByConceptId(conceptId);
    Set<String> questionIds = conceptRepository.findQuestionIdsByConceptId(conceptId);
    if (!instrumentIds.isEmpty() || !questionIds.isEmpty()) {
      throw new ConceptInUseException(instrumentIds, questionIds);
    } else {
      conceptRepository.deleteById(conceptId);
    }
  }
}
