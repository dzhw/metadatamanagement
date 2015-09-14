package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.Field;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * This class is a representation of a survey. This is a nested object of a survey variable.
 * 
 * @see VariableDocument
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders")
public class VariableSurvey {

  // Public constants which are used in queries as fieldnames.
  public static final Field SURVEY_ID_FIELD = new Field("surveyId");
  public static final Field TITLE_FIELD = new Field("title").withTermFilter().withAggregation();
  public static final Field SURVEY_PERIOD_FIELD = new Field("surveyPeriod");
  public static final Field VARIABLE_ALIAS_FIELD = new Field("variableAlias");

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

  /**
   * The alias is by default a copy of the {@code VariableDocument.getName()}. It will be used for
   * the front end. If the alias is different from the {@code VariableDocument.getName()}, the
   * system displays this alias.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String variableAlias;

  public VariableSurvey() {
    this.surveyPeriod = new DateRange();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("surveyId", surveyId).add("title", title)
        .add("surveyPeriod", surveyPeriod).add("variableAlias", variableAlias).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(surveyId, title, surveyPeriod, variableAlias);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      VariableSurvey that = (VariableSurvey) object;
      return Objects.equal(this.surveyId, that.surveyId) && Objects.equal(this.title, that.title)
          && Objects.equal(this.surveyPeriod, that.surveyPeriod)
          && Objects.equal(this.variableAlias, that.variableAlias);
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

  public String getVariableAlias() {
    return variableAlias;
  }

  public void setVariableAlias(String alias) {
    this.variableAlias = alias;
  }

  public DateRange getSurveyPeriod() {
    return surveyPeriod;
  }

  public void setSurveyPeriod(DateRange surveyPeriod) {
    this.surveyPeriod = surveyPeriod;
  }
}
