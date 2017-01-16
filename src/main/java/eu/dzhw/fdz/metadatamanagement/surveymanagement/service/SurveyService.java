package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.domain.ElasticsearchUpdateQueueAction;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchType;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchUpdateQueueService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

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
  private ElasticsearchUpdateQueueService elasticsearchUpdateQueueService;
  
  @Autowired 
  private SurveyResponseRateImageService imageService;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteAllSurveysByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of surveys within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   */
  public void deleteAllSurveysByProjectId(String dataAcquisitionProjectId) {
    List<Survey> deletedSurveys =
        surveyRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedSurveys.forEach(survey -> {
      this.imageService.deleteAllSurveyImagesById(survey.getId());
      elasticsearchUpdateQueueService.enqueue(
          survey.getId(), 
          ElasticsearchType.surveys, 
          ElasticsearchUpdateQueueAction.DELETE);      
    });
  }
   
  /**
   * Enqueue deletion of survey search document when the survey is deleted.
   * 
   * @param survey the deleted survey.
   */
  @HandleAfterDelete
  public void onSurveyDeleted(Survey survey) {
    this.imageService.deleteAllSurveyImagesById(survey.getId());
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
}
