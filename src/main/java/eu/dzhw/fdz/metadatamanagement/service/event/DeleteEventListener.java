package eu.dzhw.fdz.metadatamanagement.service.event;

import javax.inject.Inject;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.service.VariableService;

/**
 * Handles deletion events from a survey.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
public class DeleteEventListener {

  @Inject
  private VariableService variableService;
  
  /**
   * Deletes variables with the survey id.
   * 
   * @param deletionEvent A survey deletion event.
   */
  @EventListener
  public void deleteSurvey(SurveyDeleteEvent deletionEvent) {
    this.variableService.deleteBySurveyId(deletionEvent.getSurveyId());
  }

}
