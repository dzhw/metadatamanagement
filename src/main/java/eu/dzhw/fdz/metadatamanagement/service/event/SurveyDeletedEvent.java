package eu.dzhw.fdz.metadatamanagement.service.event;

import org.springframework.context.ApplicationEvent;

import eu.dzhw.fdz.metadatamanagement.service.SurveyService;

/**
 * This Event will be fired by the {@link SurveyService}, if a survey has been deleted.
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyDeletedEvent extends ApplicationEvent {

  private static final long serialVersionUID = 1L;

  private String surveyId;

  /**
   * Constructor for the SurveyDeletionEvent.
   * 
   * @param surveyId The id of the survey, which will be delete.
   */
  public SurveyDeletedEvent(String surveyId) {
    super(surveyId);
    this.surveyId = surveyId;
  }

  /* GETTER / SETTER */
  public String getSurveyId() {
    return surveyId;
  }
}
