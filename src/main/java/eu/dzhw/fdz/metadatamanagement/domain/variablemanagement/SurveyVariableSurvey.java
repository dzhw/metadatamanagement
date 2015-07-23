/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import java.time.LocalDate;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The representation of a survey. This can be a nested object of a survey variable.
 * 
 * @see SurveyVariable
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyVariableSurvey {

  /**
   * The primary key for a survey.
   */
  @Id
  @Size(min = 1, max = 32)
  @NotEmpty
  private String surveyID;

  /**
   * The title of a survey
   */
  @Size(min = 1, max = 32)
  @NotEmpty
  private String title;

  /**
   * The start date of the survey. The checked pattern is: {@code yyyy-MM-dd}
   */
  // TODO Before(end)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  /**
   * The end date of the survey. The checked pattern is: {@code yyyy-MM-dd}
   */
  // TODO After(end)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  /* GETTER / SETTER */

  /**
   * @return the surveyID
   */
  public String getSurveyID() {
    return surveyID;
  }

  /**
   * @param surveyID the surveyID to set
   */
  public void setSurveyID(String surveyID) {
    this.surveyID = surveyID;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the startDate
   */
  public LocalDate getStartDate() {
    return startDate;
  }

  /**
   * @param startDate the startDate to set
   */
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public LocalDate getEndDate() {
    return endDate;
  }

  /**
   * @param endDate the endDate to set
   */
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
