package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import java.time.LocalDate;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * This class is a representation of a survey. This can be a nested object of a survey variable.
 * 
 * @see SurveyVariable
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyVariableSurvey {

  /**
   * The surveyID is a primary.
   */
  @Id
  @Size(min = 1, max = 32)
  @NotEmpty
  private String surveyId;

  /**
   * This holds the title of a survey.
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

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }
}
