package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
  @Size(max = 32)
  @NotEmpty
  private String surveyId;

  /**
   * This holds the title of a survey.
   */
  @Size(max = 32)
  @NotEmpty
  private String title;

  /**
   * DateRange is the representation of the range of the survey. This is a nested object.
   */
  @DateOrder
  @Field(type = FieldType.Object)
  private DateRange dateRange;

  /**
   * Output is a summarize of survey of a variable. Example:
   * {@code SurveyVariableSurvey [surveyId=SId123, title=ExampleTitle, 
   * DateRange [StartDate=2015-07-22, EndDate=2015-07-24]]}
   * 
   * @return A String which will summarizes the object date range.
   */
  @Override
  public String toString() {
    return "SurveyVariableSurvey [surveyId=" + this.surveyId + ", title=" + this.title + ", "
        + this.dateRange.toString() + "]";
  }


  /* GETTER / SETTER */
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
