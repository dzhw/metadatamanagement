package eu.dzhw.fdz.metadatamanagement.studymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

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

  /**
   * Delete all studies when the dataAcquisitionProject was deleted.
   * 
   * @param dataAcquisitionProject the dataAcquisitionProject which has been deleted.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllStudiesByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of studies within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted studies
   */
  public List<Study> deleteAllStudiesByProjectId(String dataAcquisitionProjectId) {
    List<Study> deletedStudies =
        this.studyRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedStudies.forEach(study -> {
      elasticsearchUpdateQueueService.enqueue(
          study.getId(), 
          ElasticsearchType.studies, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
    return deletedStudies;
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
    if (dataSet.getStudyId() != null) {      
      Study study = studyRepository.findOne(dataSet.getStudyId());
      if (study != null) {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      }
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
    if (variable.getStudyId() != null) {
      Study study = studyRepository.findOne(variable.getStudyId());
      if (study != null) {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      }      
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
      List<Study> studies = studyRepository.findByIdIn(relatedPublication.getStudyIds());
      studies.forEach(study -> {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);            
      });      
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
  public void onSurveyChanged(Survey survey) {
    if (survey.getStudyId() != null) {
      Study study = studyRepository.findOne(survey.getStudyId());
      if (study != null) {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      }      
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
    if (question.getStudyId() != null) {
      Study study = studyRepository.findOne(question.getStudyId());
      if (study != null) {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      }      
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
    if (instrument.getStudyId() != null) {
      Study study = studyRepository.findOne(instrument.getStudyId());
      if (study != null) {
        elasticsearchUpdateQueueService.enqueue(
            study.getId(), 
            ElasticsearchType.studies, 
            ElasticsearchUpdateQueueAction.UPSERT);      
      }      
    }
  }
}
