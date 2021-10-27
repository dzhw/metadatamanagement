package eu.dzhw.fdz.metadatamanagement.conceptmanagement.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.service.CrudService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptInUseException;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.InstrumentSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentChangesProvider;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyQueueItemService;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.ShadowCopyingEndedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.RelatedQuestionSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections.VariableSubDocumentProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the domain object/aggregate {@link Concept}.
 *
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
@RequiredArgsConstructor
public class ConceptManagementService implements CrudService<Concept> {

  private final ConceptRepository conceptRepository;

  private final QuestionRepository questionRepository;

  private final InstrumentRepository instrumentRepository;

  private final VariableRepository variableRepository;

  private final ConceptAttachmentService conceptAttachmentService;

  private final ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  private final InstrumentChangesProvider instrumentChangesProvider;

  private final QuestionChangesProvider questionChangesProvider;

  private final ConceptCrudHelper crudHelper;

  /**
   * Enqueue update of concept search documents when the dataPackage changed.
   *
   * @param dataPackage the updated, created or deleted dataPackage.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataPackageChanged(DataPackage dataPackage) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      List<InstrumentSubDocumentProjection> instruments =
          instrumentRepository.findSubDocumentsByDataPackageId(dataPackage.getId());
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
    Set<String> removedConceptIds = questionChangesProvider.getRemovedConceptIds(question.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      if (question.getConceptIds() != null) {
        conceptIds.addAll(question.getConceptIds());
        conceptIds.addAll(removedConceptIds);
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
    Set<String> removedConceptIds =
        instrumentChangesProvider.getRemovedConceptIds(instrument.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      if (instrument.getConceptIds() != null) {
        conceptIds.addAll(instrument.getConceptIds());
        conceptIds.addAll(removedConceptIds);
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
   * Re-indexes concepts with new instrument and question references if a shadow copy of a project
   * is saved.
   *
   * @param shadowCopyingEndedEvent Event emitted by {@link ShadowCopyQueueItemService}.
   */
  @EventListener
  public void onShadowCopyingEnded(ShadowCopyingEndedEvent shadowCopyingEndedEvent) {
    String projectId = shadowCopyingEndedEvent.getDataAcquisitionProjectId() + "-"
        + shadowCopyingEndedEvent.getRelease().getVersion();
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {
      Set<String> conceptIds = new HashSet<>();
      instrumentRepository.streamByDataAcquisitionProjectId(projectId).forEach(instrument -> {
        if (instrument.getConceptIds() != null) {
          conceptIds.addAll(instrument.getConceptIds());
        }
        questionRepository.findSubDocumentsByInstrumentId(instrument.getId()).forEach(question -> {
          if (question.getConceptIds() != null) {
            conceptIds.addAll(question.getConceptIds());
          }
        });
      });
      return conceptRepository.streamIdsByIdIn(conceptIds);
    }, ElasticsearchType.concepts);
  }

  @Override
  public Optional<Concept> read(String id) {
    return crudHelper.read(id);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public void delete(Concept concept) {
    Set<String> instrumentIds = conceptRepository.findInstrumentIdsByConceptId(concept.getId());
    Set<String> questionIds = conceptRepository.findQuestionIdsByConceptId(concept.getId());
    if (!instrumentIds.isEmpty() || !questionIds.isEmpty()) {
      throw new ConceptInUseException(instrumentIds, questionIds);
    }
    crudHelper.delete(concept);
    conceptAttachmentService.deleteAllByConceptId(concept.getId());
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public Concept save(Concept concept) {
    return crudHelper.save(concept);
  }

  @Override
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public Concept create(Concept concept) {
    return crudHelper.create(concept);
  }

  @Override
  public Optional<Concept> readSearchDocument(String id) {
    return crudHelper.readSearchDocument(id);
  }
}
