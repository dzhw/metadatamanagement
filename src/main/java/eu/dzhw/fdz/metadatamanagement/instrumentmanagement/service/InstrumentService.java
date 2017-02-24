package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RelatedQuestion;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

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
  private InstrumentAttachmentService instrumentAttachmentService;

  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;

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
   * A service method for deletion of instruments within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllInstrumentsByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Instrument> instruments = instrumentRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      instruments.forEach(instrument -> {
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
    elasticsearchUpdateQueueService.enqueue(
        instrument.getId(), 
        ElasticsearchType.instruments, 
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
    elasticsearchUpdateQueueService.enqueue(
        instrument.getId(), 
        ElasticsearchType.instruments, 
        ElasticsearchUpdateQueueAction.UPSERT);
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
    enqueueUpserts(instrumentRepository.streamIdsByStudyId(study.getId()));
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
    enqueueUpserts(instrumentRepository.streamIdsBySurveyIdsContaining(survey.getId()));
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
    IdAndVersionProjection instrument = instrumentRepository
        .findOneIdAndVersionById(question.getInstrumentId());
    if (instrument != null) {
      elasticsearchUpdateQueueService.enqueue(
          instrument.getId(), 
          ElasticsearchType.instruments, 
          ElasticsearchUpdateQueueAction.UPSERT);                
    }
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
    if (variable.getRelatedQuestions() != null) {
      List<String> instrumentIds = variable.getRelatedQuestions().stream()
          .map(RelatedQuestion::getInstrumentId).collect(Collectors.toList());
      enqueueUpserts(instrumentRepository.streamIdsByIdIn(instrumentIds));
    }
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
    if (relatedPublication.getInstrumentIds() != null) {
      enqueueUpserts(instrumentRepository.streamIdsByIdIn(
          relatedPublication.getInstrumentIds()));
    }
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> instruments) {
    try (Stream<IdAndVersionProjection> instrumentStream = instruments) {
      instrumentStream.forEach(instrument -> {
        elasticsearchUpdateQueueService.enqueue(instrument.getId(),
            ElasticsearchType.instruments, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}
