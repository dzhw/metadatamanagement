package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.data.rest.core.event.BeforeDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.ShadowCopyService;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.projections.IdAndNumberInstrumentProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ProjectReleasedEvent;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationChangesProvider;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableChangesProvider;

/**
 * The service for the instruments. This service handels delete events.
 * 
 * @author René Reitmann
 *
 */
@Service
@RepositoryEventHandler
public class InstrumentService {

  @Autowired
  private InstrumentRepository instrumentRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private InstrumentChangesProvider instrumentChangesProvider;

  @Autowired
  private VariableChangesProvider variableChangesProvider;

  @Autowired
  private InstrumentAttachmentService instrumentAttachmentService;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;

  @Autowired
  private ApplicationEventPublisher eventPublisher;

  @Autowired
  private RelatedPublicationChangesProvider relatedPublicationChangesProvider;

  @Autowired
  private ShadowCopyService<Instrument> shadowCopyService;

  @Autowired
  private InstrumentShadowCopyDataSource instrumentShadowCopyDataSource;

  /**
   * Delete all instruments when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllInstrumentsByProjectId(dataAcquisitionProject.getId());
  }

  /**
   * Update all Instruments of the project, when the project is released.
   * 
   * @param dataAcquisitionProject the changed project
   */
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository
            .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * A service method for deletion of instruments within a data acquisition project.
   * 
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllInstrumentsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Instrument> instruments =
        instrumentRepository.streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      instruments.forEach(instrument -> {
        if (instrument.isShadow()) {
          throw new ShadowCopyDeleteNotAllowedException();
        }
        eventPublisher.publishEvent(new BeforeDeleteEvent(instrument));
        instrumentRepository.delete(instrument);
        eventPublisher.publishEvent(new AfterDeleteEvent(instrument));
      });
    }
  }

  /**
   * Enqueue deletion of instrument search document when the instrument is deleted.
   * 
   * @param instrument the deleted instrument.
   */
  @HandleAfterDelete
  public void onInstrumentDeleted(Instrument instrument) {
    instrumentAttachmentService.deleteAllByInstrumentId(instrument.getId());
    elasticsearchUpdateQueueService.enqueue(instrument.getId(), ElasticsearchType.instruments,
        ElasticsearchUpdateQueueAction.DELETE);
  }

  /**
   * Enqueue update of instrument search document when the instrument is updated.
   * 
   * @param instrument the updated or created instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onInstrumentSaved(Instrument instrument) {
    elasticsearchUpdateQueueService.enqueue(instrument.getId(), ElasticsearchType.instruments,
        ElasticsearchUpdateQueueAction.UPSERT);
  }

  /**
   * Remember the old and new instrument.
   * 
   * @param instrument the new instrument
   */
  @HandleBeforeSave
  public void onBeforeInstrumentSaved(Instrument instrument) {
    instrumentChangesProvider.put(instrument,
        instrumentRepository.findById(instrument.getId()).get());
  }

  @HandleBeforeCreate
  public void onBeforeInstrumentCreated(Instrument instrument) {
    instrumentChangesProvider.put(instrument, null);
  }

  @HandleBeforeDelete
  public void onBeforeInstrumentDeleted(Instrument instrument) {
    instrumentChangesProvider.put(null, instrument);
  }

  /**
   * Enqueue update of instrument search documents when the study changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByStudyId(study.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the survey changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onSurveyChanged(Survey survey) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsBySurveyIdsContaining(survey.getId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the question changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    elasticsearchUpdateQueueService.enqueueUpsertAsync(
        () -> instrumentRepository.findOneIdAndVersionById(question.getInstrumentId()),
        ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the variable changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    List<String> instrumentIds = variableChangesProvider.getAffectedInstrumentIds(variable.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByIdIn(instrumentIds), ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the related publication changed.
   * 
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    List<String> instrumentIds =
        relatedPublicationChangesProvider.getAffectedInstrumentIds(relatedPublication.getId());
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByMasterIdIn(instrumentIds), ElasticsearchType.instruments);
  }

  /**
   * Enqueue update of instrument search documents when the concept is changed.
   * 
   * @param concept the updated, created or deleted concept.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onConceptChanged(Concept concept) {
    elasticsearchUpdateQueueService.enqueueUpsertsAsync(
        () -> instrumentRepository.streamIdsByConceptIdsContaining(concept.getId()),
        ElasticsearchType.instruments);

    elasticsearchUpdateQueueService.enqueueUpsertsAsync(() -> {      
      Set<String> instrumentIds = questionRepository.streamIdsByConceptIdsContaining(concept.getId())
          .map(question -> question.getInstrumentId()).collect(Collectors.toSet());
      return instrumentRepository.streamIdsByIdIn(instrumentIds);
    }, ElasticsearchType.instruments);
  }

  /**
   * Create shadow copies for {@link Instrument} on project release.
   * 
   * @param projectReleasedEvent Released project event
   */
  @EventListener
  public void onProjectReleaseEvent(ProjectReleasedEvent projectReleasedEvent) {
    shadowCopyService.createShadowCopies(projectReleasedEvent.getDataAcquisitionProject(),
        projectReleasedEvent.getPreviousReleaseVersion(), instrumentShadowCopyDataSource);
  }

  /**
   * Get a list of available instrument numbers for creating a new instrument.
   * 
   * @param dataAcquisitionProjectId The project id.
   * @return A list of available instrument numbers.
   */
  public List<Integer> getFreeInstrumentNumbers(String dataAcquisitionProjectId) {
    List<Integer> result = new ArrayList<>();
    List<IdAndNumberInstrumentProjection> existingNumbers = instrumentRepository
        .findInstrumentNumbersByDataAcquisitionProjectId(dataAcquisitionProjectId);
    Optional<IdAndNumberInstrumentProjection> max = existingNumbers.stream().max((instrument1,
        instrument2) -> Integer.compare(instrument1.getNumber(), instrument2.getNumber()));
    if (!max.isPresent()) {
      result.add(1);
    } else {
      for (int i = 1; i < max.get().getNumber(); i++) {
        if (!instrumentNumberExists(existingNumbers, i)) {
          result.add(i);
        }
      }
      result.add(max.get().getNumber() + 1);
    }
    return result;
  }

  private boolean instrumentNumberExists(List<IdAndNumberInstrumentProjection> instruments,
      Integer number) {
    Predicate<IdAndNumberInstrumentProjection> predicate =
        instrument -> instrument.getNumber().equals(number);
    return instruments.stream().filter(predicate).findFirst().isPresent();
  }
}
