package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidScaleLevel;

/**
 * This is a representation of a variable. All fields describe the attributes of the variable, for
 * example the possible answers, the labels or the data type.
 * 
 * @author Daniel Katzberg
 *
 */
@Document(
    indexName = "#{'" + AbstractDocument.METADATA_INDEX + "_'"
        + "+T(org.springframework.context.i18n.LocaleContextHolder).getLocale().getLanguage()}",
    type = "variables")
public class VariableDocument extends AbstractDocument {



  /**
   * This is a nested reference to the survey.
   */
  @Field(type = FieldType.Nested)
  @Valid
  private VariableSurvey variableSurvey;

  /**
   * The name of the variable.
   */
  @Size(max = 32)
  @NotEmpty
  private String name;

  /**
   * The data type of the variable.
   */
  @ValidDataType
  private String dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80)
  private String label;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  // TODO validate not null for variables with data type numeric
  @ValidScaleLevel
  private String scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Field(type = FieldType.Nested)
  @Valid
  private List<AnswerOption> answerOptions;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VariableDocument [getFdzId()=" + getId() + ", getVariableSurveyDocument()="
        + getVariableSurveyDocument() + ", getName()=" + getName() + ", getLabel()=" + getLabel()
        + ", getDataType()=" + getDataType() + ", getScaleLevel()=" + getScaleLevel()
        + ", getAnswerOptions()=" + getAnswerOptions() + "]";
  }


  /* GETTER / SETTER */

  public VariableSurvey getVariableSurveyDocument() {
    return variableSurvey;
  }

  public void setVariableSurveyDocument(VariableSurvey variableSurvey) {
    this.variableSurvey = variableSurvey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  protected String getDataType() {
    return dataType;
  }

  protected void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public List<AnswerOption> getAnswerOptions() {
    return answerOptions;
  }

  public void setAnswerOptions(List<AnswerOption> answerOptions) {
    this.answerOptions = answerOptions;
  }

}
