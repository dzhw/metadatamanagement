package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.NotNullScaleLevelOnNumericDataType;
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
@NotNullScaleLevelOnNumericDataType
public class VariableDocument extends AbstractDocument {

  // Public constants which are used in queries as fieldnames.
  public static final String ALL_STRINGS_AS_NGRAMS_FIELD = "allStringsAsNgrams";
  public static final String NAME_FIELD = "name";
  public static final String DATA_TYPE_FIELD = "dataType";
  public static final String LABEL_FIELD = "label";
  public static final String SCALE_LEVEL_FIELD = "scaleLtevel";
  public static final String ANSWER_OPTIONS_FIELD = "answerOptions";
  public static final String VARIABLE_SURVEY_FIELD = "variableSurvey";
  public static final String NESTED_VARIABLE_SURVEY_TITLE_FIELD =
      VARIABLE_SURVEY_FIELD + "." + VariableSurvey.TITLE_FIELD;
  public static final String NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD =
      VARIABLE_SURVEY_FIELD + "." + VariableSurvey.VARIABLE_ALIAS_FIELD;
  public static final String NESTED_VARIABLE_SURVEY_ID_FIELD =
      VARIABLE_SURVEY_FIELD + "." + VariableSurvey.SURVEY_ID_FIELD;
  public static final String NESTED_VARIABLE_SURVEY_PERIOD_FIELD =
      VARIABLE_SURVEY_FIELD + "." + VariableSurvey.SURVEY_PERIOD_FIELD;
  public static final String NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE = VARIABLE_SURVEY_FIELD
      + "." + VariableSurvey.SURVEY_PERIOD_FIELD + "." + DateRange.STARTDATE_FIELD;
  public static final String NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE = VARIABLE_SURVEY_FIELD
      + "." + VariableSurvey.SURVEY_PERIOD_FIELD + "." + DateRange.ENDDATE_FIELD;
  public static final String NESTED_ANSWER_OPTIONS_CODE_FIELD =
      ANSWER_OPTIONS_FIELD + "." + AnswerOption.CODE_FIELD;
  public static final String NESTED_ANSWER_OPTIONS_LABEL_FIELD =
      ANSWER_OPTIONS_FIELD + "." + AnswerOption.LABEL_FIELD;
  /**
   * This is a nested reference to the survey.
   */
  @Valid
  private VariableSurvey variableSurvey;

  /**
   * The name of the variable.
   */
  @Size(max = 32)
  @NotBlank
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
  @ValidScaleLevel
  private String scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Valid
  private List<AnswerOption> answerOptions;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VariableDocument [variableSurvey=" + variableSurvey + ", name=" + name + ", dataType="
        + dataType + ", label=" + label + ", scaleLevel=" + scaleLevel + ", answerOptions="
        + answerOptions + "]";
  }


  /* GETTER / SETTER */

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

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
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
