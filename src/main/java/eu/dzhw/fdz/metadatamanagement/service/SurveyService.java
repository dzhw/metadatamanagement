package eu.dzhw.fdz.metadatamanagement.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.data.rest.core.event.AfterDeleteEvent;
import org.springframework.stereotype.Service;

import eu.dzhw.fdz.metadatamanagement.domain.FdzProject;
import eu.dzhw.fdz.metadatamanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * Service which deletes surveys when the corresponding fdzProject has been deleted.
 * 
 * @author René Reitmann
 */
@Service
@RepositoryEventHandler
public class SurveyService {
  @Inject
  private SurveyRepository surveyRepository;
  @Inject
  private ApplicationEventPublisher eventPublisher;
  
  @HandleAfterDelete
  public void onFdzProjectDeleted(FdzProject fdzProject) {
    List<Survey> deletedSurveys = surveyRepository.deleteByFdzProject(fdzProject);
    deletedSurveys.forEach(survey -> eventPublisher.publishEvent(new AfterDeleteEvent(survey)));
  }
}
