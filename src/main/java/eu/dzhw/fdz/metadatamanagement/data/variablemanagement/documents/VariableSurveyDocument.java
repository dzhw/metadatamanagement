package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import eu.dzhw.fdz.metadatamanagement.data.common.validation.ValidDateRange;

/**
 * This class is a representation of a survey. This is a nested object of a survey variable.
 * 
 * @see VariableDocument
 * 
 * @author Daniel Katzberg
 *
 */
public class VariableSurveyDocument {

  /**
   * The surveyID is a primary.
   */
  @Size(max = 32)
  @NotEmpty
  private String surveyId;

  /**
   * This holds the title of a survey.
   */
  @NotEmpty
  @Size(max = 32)
  private String title;

  /**
   * DateRange is the representation of the range of the survey. This is a nested object.
   */
  @Field(type = FieldType.Object)
  @ValidDateRange
  private DateRange surveyPeriod;

  /**
   * The alias is by default a copy of the {@code VariableDocument.getName()}. It will be used for
   * the front end. If the alias is different from the {@code VariableDocument.getName()}, the
   * system displays this alias.
   */
  @Size(max = 32)
  @NotEmpty
  // TODO validate that the name is unique within a survey
  private String alias;

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VariableSurveyDocument [getSurveyId()=" + getSurveyId() + ", getTitle()=" + getTitle()
        + ", getDateRange()=" + getSurveyId() + ", getAlias()=" + getAlias() + "]";
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

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public DateRange getSurveyPeriod() {
    return surveyPeriod;
  }

  public void setSurveyPeriod(DateRange surveyPeriod) {
    this.surveyPeriod = surveyPeriod;
  }  
}
