package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.DateOrder;

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
  
  @DateOrder
  //TODO Nested
  private DateRange dateRange;


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

  public DateRange getDateRange() {
    return dateRange;
  }

  public void setDateRange(DateRange dateRange) {
    this.dateRange = dateRange;
  }  
}
