package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.enumclasses.ScaleLevel;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.Value;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.Enum;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.ListSizeComparement;

/**
 * This is a representation of a variable. All fields describe the attributes of the variable, for
 * example the possible answers, the lables or the data type.
 * 
 * 
 * @author Daniel Katzberg
 *
 */
public class SurveyVariable {

  /**
   * A fdzID as primary key for the identification of the variable of a survey.
   */
  @Id
  @Size(min = 1, max = 32)
  @NotEmpty
  private String fdzId;

  // TODO nested
  private SurveyVariableSurvey surveyVariableSurvey;

  /**
   * The name of the variable.
   */
  @Size(min = 1, max = 32)
  @NotEmpty
  private String name;

  /**
   * The data type of the variable.
   */
  @Enum(enumClass = DataTypes.class)
  private String dataType;

  /**
   * The lable of the variable.
   */
  @Size(max = 80)
  private String lable;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  @Enum(enumClass = ScaleLevel.class)
  private String scaleLevel;

  //TODO nested
  @ListSizeComparement
  private Value value;

  public String getFdzId() {
    return fdzId;
  }

  public void setFdzId(String fdzId) {
    this.fdzId = fdzId;
  }

  public SurveyVariableSurvey getSurveyVariableSurvey() {
    return surveyVariableSurvey;
  }

  public void setSurveyVariableSurvey(SurveyVariableSurvey surveyVariableSurvey) {
    this.surveyVariableSurvey = surveyVariableSurvey;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getLable() {
    return lable;
  }

  public void setLable(String lable) {
    this.lable = lable;
  }

  public String getScaleLevel() {
    return scaleLevel;
  }

  public void setScaleLevel(String scaleLevel) {
    this.scaleLevel = scaleLevel;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }
}
