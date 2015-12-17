package eu.dzhw.fdz.metadatamanagement.service.event;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.service.SurveyService;
import eu.dzhw.fdz.metadatamanagement.service.VariableService;

/**
 * Handles delete events between different services.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
public class DeleteEventListener {

  @Inject
  private VariableService variableService;

  @Inject
  private SurveyService surveyService;

  /**
   * Deletes variables with the survey id.
   * 
   * @param surveyDeleteEvent A survey delete event.
   */
  @EventListener
  public void deleteSurvey(SurveyDeleteEvent surveyDeleteEvent) {
    this.variableService.deleteBySurveyId(surveyDeleteEvent.getSurveyId());
  }

  /**
   * Deletes surveys and variables with the fdz project name.
   * 
   * @param fdzProjectDeletionEvent A fdz project delete event.
   */
  @EventListener
  public void deleteSurvey(FdzProjectDeleteEvent fdzProjectDeletionEvent) {
    this.surveyService.deleteByFdzProjectName(fdzProjectDeletionEvent.getFdzProjectName());
    this.variableService.deleteByFdzProjectName(fdzProjectDeletionEvent.getFdzProjectName());
  }

}
