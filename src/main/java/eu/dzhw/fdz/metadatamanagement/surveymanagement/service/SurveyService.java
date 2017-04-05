package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
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
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Service which deletes surveys when the corresponding dataAcquisitionProject has been deleted.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class SurveyService {
  @Autowired
  private SurveyRepository surveyRepository;
  
  @Autowired
  private InstrumentRepository instrumentRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired 
  private SurveyResponseRateImageService imageService;
  
  @Autowired 
  private SurveyAttachmentService surveyAttachmentService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllSurveysByProjectId(dataAcquisitionProject.getId());
  }
  
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    enqueueUpserts(surveyRepository
        .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()));
  }
  
  /**
   * A service method for deletion of surveys within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllSurveysByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Survey> surveys = surveyRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      surveys.forEach(survey -> {
        surveyRepository.delete(survey);
        eventPublisher.publishEvent(new AfterDeleteEvent(survey));
      });
    }
  }
   
  /**
   * Enqueue deletion of survey search document when the survey is deleted.
   * 
   * @param survey the deleted survey.
   */
  @HandleAfterDelete
  public void onSurveyDeleted(Survey survey) {
    this.imageService.deleteAllSurveyImagesById(survey.getId());
    this.surveyAttachmentService.deleteAllBySurveyId(survey.getId());
    elasticsearchUpdateQueueService.enqueue(
        survey.getId(), 
        ElasticsearchType.surveys, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of survey search document when the survey is updated.
   * 
   * @param survey the updated or created survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onSurveySaved(Survey survey) {
    elasticsearchUpdateQueueService.enqueue(
        survey.getId(), 
        ElasticsearchType.surveys, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of survey search documents when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    if (instrument.getSurveyIds() != null) {
      enqueueUpserts(surveyRepository.streamIdsByIdIn(instrument.getSurveyIds()));
    }
  }
  
  /**
   * Enqueue update of survey search documents when the question is changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    Instrument instrument = instrumentRepository.findOne(question.getInstrumentId());
    if (instrument != null && instrument.getSurveyIds() != null) {
      enqueueUpserts(surveyRepository.streamIdsByIdIn(instrument.getSurveyIds()));
    }
  }
  
  /**
   * Enqueue update of survey search documents when the study is changed.
   * 
   * @param study the updated, created or deleted study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onStudyChanged(Study study) {
    enqueueUpserts(surveyRepository.streamIdsByStudyId(study.getId()));
  }
  
  /**
   * Enqueue update of survey search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onVariableChanged(Variable variable) {
    if (variable.getSurveyIds() != null) {
      enqueueUpserts(surveyRepository.streamIdsByIdIn(variable.getSurveyIds()));
    }
  }
  
  /**
   * Enqueue update of survey search document when the dataSet is updated.
   * 
   * @param dataSet the updated or created dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    if (dataSet.getSurveyIds() != null) {
      enqueueUpserts(surveyRepository.streamIdsByIdIn(dataSet.getSurveyIds()));
    }
  }
  
  /**
   * Enqueue update of survey search document when the related publication is updated.
   * 
   * @param relatedPublication the updated or created publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    if (relatedPublication.getSurveyIds() != null) {
      enqueueUpserts(surveyRepository
          .streamIdsByIdIn(relatedPublication.getSurveyIds()));
            
    }
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> surveys) {
    try (Stream<IdAndVersionProjection> surveyStream = surveys) {
      surveyStream.forEach(survey -> {
        elasticsearchUpdateQueueService.enqueue(survey.getId(),
            ElasticsearchType.surveys, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}
