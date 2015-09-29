package eu.dzhw.fdz.metadatamanagement.data.common.documents;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;

/**
 * An abstract representations of the nested survey information. e.g. for variables or questions.
 * 
 * @author Daniel Katzberg
 *
 */
public abstract class AbstractNestedSurvey {

  // Public constants which are used in queries as fieldnames.
  public static final String SURVEY_ID_FIELD = "surveyId";
  public static final String TITLE_FIELD = "title";
  public static final String SURVEY_PERIOD_FIELD = "surveyPeriod";
  
  /**
   * The surveyID is a primary.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String surveyId;

  /**
   * This holds the title of a survey.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String title;

  /**
   * DateRange is the representation of the range of the survey. This is a nested object.
   */
  @Valid
  @NotNull(groups = {Create.class, Edit.class})
  private DateRange surveyPeriod;
  
  public AbstractNestedSurvey() {
    this.surveyPeriod = new DateRange();
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("surveyId", surveyId).add("title", title)
        .add("surveyPeriod", surveyPeriod).toString();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(surveyId, title, surveyPeriod);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      AbstractNestedSurvey that = (AbstractNestedSurvey) object;
      return Objects.equal(this.surveyId, that.surveyId) && Objects.equal(this.title, that.title)
          && Objects.equal(this.surveyPeriod, that.surveyPeriod);
    }
    return false;
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
  
  public DateRange getSurveyPeriod() {
    return surveyPeriod;
  }

  public void setSurveyPeriod(DateRange surveyPeriod) {
    this.surveyPeriod = surveyPeriod;
  }
}
