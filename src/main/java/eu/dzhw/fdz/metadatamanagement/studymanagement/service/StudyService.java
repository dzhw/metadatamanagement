package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

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
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.studymanagement.repository.StudyRepository;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Service for creating and updating variable. Used for updating variables in mongo and
 * elasticsearch.
 * 
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class StudyService {

  @Autowired
  private StudyRepository studyRepository;
  
  @Autowired
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired
  private ApplicationEventPublisher eventPublisher;

  /**
   * Delete all studies when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllStudiesByProjectId(dataAcquisitionProject.getId());
  }
  
  @HandleAfterSave
  public void onDataAcquisitionProjectUpdated(DataAcquisitionProject dataAcquisitionProject) {
    enqueueUpserts(studyRepository
        .streamIdsByDataAcquisitionProjectId(dataAcquisitionProject.getId()));
  }
  
  /**
   * A service method for deletion of studies within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllStudiesByProjectId(String dataAcquisitionProjectId) {
    try (Stream<Study> studies = studyRepository
        .streamByDataAcquisitionProjectId(dataAcquisitionProjectId)) {
      studies.forEach(study -> {
        studyRepository.delete(study);
        eventPublisher.publishEvent(new AfterDeleteEvent(study));
      });      
    }
  }
  
  /**
   * Enqueue deletion of study search document when the study is deleted.
   * 
   * @param study the deleted variable.
   */
  @HandleAfterDelete
  public void onStudyDeleted(Study study) {
    elasticsearchUpdateQueueService.enqueue(
        study.getId(), 
        ElasticsearchType.studies, 
        ElasticsearchUpdateQueueAction.DELETE);
  }
  
  /**
   * Enqueue update of study search document when the study is updated.
   * 
   * @param study the updated or created study.
   */
  @HandleAfterCreate
  @HandleAfterSave
  public void onStudySaved(Study study) {
    elasticsearchUpdateQueueService.enqueue(
        study.getId(), 
        ElasticsearchType.studies, 
        ElasticsearchUpdateQueueAction.UPSERT);
  }
  
  /**
   * Enqueue update of study search document when the data set is changed.
   * 
   * @param dataSet the updated, created or deleted dataSet.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onDataSetChanged(DataSet dataSet) {
    IdAndVersionProjection study = studyRepository.findOneIdAndVersionById(dataSet.getStudyId());
    if (study != null) {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    }   
  }
  
  /**
   * Enqueue update of study search document when the variable is changed.
   * 
   * @param variable the updated, created or deleted variable.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onVariableChanged(Variable variable) {
    IdAndVersionProjection study = studyRepository.findOneIdAndVersionById(variable.getStudyId());
    if (study != null) {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    }      
  }
  
  /**
   * Enqueue update of study search document when a related publication is changed.
   * 
   * @param relatedPublication the updated, created or deleted publication.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onRelatedPublicationChanged(RelatedPublication relatedPublication) {
    if (relatedPublication.getStudyIds() != null) {
      enqueueUpserts(studyRepository.streamIdsByIdIn(relatedPublication.getStudyIds()));      
    }
  }
  
  /**
   * Enqueue update of study search document when the survey is changed.
   * 
   * @param survey the updated, created or deleted survey.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  @Async
  public void onSurveyChanged(Survey survey) {
    IdAndVersionProjection study = studyRepository.findOneIdAndVersionById(survey.getStudyId());
    if (study != null) {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    }      
  }
  
  /**
   * Enqueue update of study search document when the question is changed.
   * 
   * @param question the updated, created or deleted question.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onQuestionChanged(Question question) {
    IdAndVersionProjection study = studyRepository.findOneIdAndVersionById(question.getStudyId());
    if (study != null) {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    }      
  }
  
  /**
   * Enqueue update of study search document when the instrument is changed.
   * 
   * @param instrument the updated, created or deleted instrument.
   */
  @HandleAfterCreate
  @HandleAfterSave
  @HandleAfterDelete
  public void onInstrumentChanged(Instrument instrument) {
    IdAndVersionProjection study = studyRepository.findOneIdAndVersionById(instrument.getStudyId());
    if (study != null) {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.UPSERT);      
    }      
  }
  
  private void enqueueUpserts(Stream<IdAndVersionProjection> studies) {
    try (Stream<IdAndVersionProjection> studyStream = studies) {
      studyStream.forEach(study -> {
        elasticsearchUpdateQueueService.enqueue(study.getId(),
            ElasticsearchType.studies, ElasticsearchUpdateQueueAction.UPSERT);
      });      
    }
  }
}
