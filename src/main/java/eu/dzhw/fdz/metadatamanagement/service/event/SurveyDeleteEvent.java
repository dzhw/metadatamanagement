package eu.dzhw.fdz.metadatamanagement.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * This Event will be fired by the SurveyService, if a survey will be delete.
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyDeleteEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private String surveyId;

  /**
   * Constructor for the SurveyDeletionEvent.
   * 
   * @param surveyId The id of the survey, which will be delete.
   */
  public SurveyDeleteEvent(String surveyId) {
    super(surveyId);
    this.surveyId = surveyId;
  }

  /* GETTER / SETTER */
  public String getSurveyId() {
    return surveyId;
  }
}
