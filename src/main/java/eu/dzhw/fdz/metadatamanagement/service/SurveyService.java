package eu.dzhw.fdz.metadatamanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * Service which deletes surveys when the corresponding dataAcquisitionProject has been deleted.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Service
@RepositoryEventHandler
public class SurveyService {
  @Inject
  private SurveyRepository surveyRepository;
  @Inject
  private ApplicationEventPublisher eventPublisher;

  /**
   * Listener, which will be activate by a deletion of a data acquisition project.
   * 
   * @param dataAcquisitionProject A reference to the data acquisition project.
   */
  @HandleAfterDelete
  public void onDataAcquisitionProjectDeleted(DataAcquisitionProject dataAcquisitionProject) {
    deleteSurveysByProjectId(dataAcquisitionProject.getId());
  }
  
  /**
   * A service method for deletion of surveys within a data acquisition project.
   * @param dataAcquisitionProjectId the id for to the data acquisition project.
   * @return List of deleted surveys
   */
  public List<Survey> deleteSurveysByProjectId(String dataAcquisitionProjectId) {
    List<Survey> deletedSurveys =
        surveyRepository.deleteByDataAcquisitionProjectId(dataAcquisitionProjectId);
    deletedSurveys.forEach(survey -> eventPublisher.publishEvent(new AfterDeleteEvent(survey)));
    return deletedSurveys;
  }
}
