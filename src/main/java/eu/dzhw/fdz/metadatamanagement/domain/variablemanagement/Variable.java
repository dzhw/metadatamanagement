package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.DataType;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.ValidValueListSize;

/**
 * This is a representation of a variable. All fields describe the attributes of the variable, for
 * example the possible answers, the labels or the data type.
 * 
 * 
 * @author Daniel Katzberg
 *
 */
@Document(indexName = "#{T(org.springframework.context.i18n.LocaleContextHolder)"
    + ".getLocale().getLanguage()+'_surveyVariable'}", type = "surveyVariable")
public class Variable {

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @Size(max = 32)
  @NotEmpty
  private String fdzId;

  /**
   * This is a nested reference to the survey.
   */
  @Field(type = FieldType.Object)
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
  @Field(index = FieldIndex.not_analyzed)
  private DataType dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80)
  private String label;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  @Field(index = FieldIndex.not_analyzed)
  private ScaleLevel scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Field(type = FieldType.Object)
  @ValidValueListSize
  private Value value;

  /**
   * Output is a summarize of a variable. Example:
   * 
   * {@codeSurvey [fdzId=fdz123, SurveyVariableSurvey [surveyId=SId123, title=ExampleTitle,
   * DateRange [StartDate=2015-07-22, EndDate=2015-07-24]], name=ExampleName, dataType=NUMERIC,
   * label=ExampleLabel, scaleLevel=METRIC, Value [Value.values.size=5, Values.valueLabels.size=5]]}
   * 
   * @return A String which will summarizes the object date range.
   */
  @Override
  public String toString() {
    return "Survey [fdzId=" + this.fdzId + ", " + this.variableSurvey.toString() + ", " + "name= "
        + this.name + ", dataType=" + this.dataType.toString() + ", label=" + this.label
        + ", scaleLevel=" + this.scaleLevel.toString() + ", " + this.value.toString() + "]";
  }

  /* GETTER / SETTER */
  public String getFdzId() {
    return fdzId;
  }

  public void setFdzId(String fdzId) {
    this.fdzId = fdzId;
  }

  public VariableSurvey getVariableSurvey() {
    return variableSurvey;
  }

  public void setVariableSurvey(VariableSurvey variableSurvey) {
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

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }

  public DataType getDataType() {
    return dataType;
  }

  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }

  public ScaleLevel getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(ScaleLevel scaleLevel) {
    this.scaleLevel = scaleLevel;
  }
}
