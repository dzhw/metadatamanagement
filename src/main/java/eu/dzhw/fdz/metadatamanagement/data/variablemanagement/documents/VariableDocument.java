package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.common.base.Objects;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.DocumentField;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.RelatedVariable;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Create;
import eu.dzhw.fdz.metadatamanagement.data.common.documents.validation.groups.ModifyValidationGroup.Edit;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.UniqueAnswerCode;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.ValidScaleLevel;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.google.common.base.MoreObjects;

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
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.builders")
public class VariableDocument extends AbstractDocument {

  // Basic Fields
  public static final DocumentField NAME_FIELD = new DocumentField("name");
  public static final DocumentField DATA_TYPE_FIELD = new DocumentField("dataType");
  public static final DocumentField LABEL_FIELD = new DocumentField("label");
  public static final DocumentField SCALE_LEVEL_FIELD = new DocumentField("scaleLevel");
  public static final DocumentField QUESTION_FIELD = new DocumentField("question");
  public static final DocumentField ANSWER_OPTIONS_FIELD = new DocumentField("answerOptions");
  public static final DocumentField VARIABLE_SURVEY_FIELD = new DocumentField("variableSurvey");

  // Nested: Variable Document - Variable Survey
  public static final DocumentField NESTED_VARIABLE_SURVEY_TITLE_FIELD =
      new DocumentField(VariableDocument.VARIABLE_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + VariableSurvey.TITLE_FIELD);

  public static final DocumentField NESTED_VARIABLE_SURVEY_VARIABLE_ALIAS_FIELD =
      new DocumentField(VariableDocument.VARIABLE_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + VariableSurvey.VARIABLE_ALIAS_FIELD);

  public static final DocumentField NESTED_VARIABLE_SURVEY_ID_FIELD =
      new DocumentField(VariableDocument.VARIABLE_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + VariableSurvey.SURVEY_ID_FIELD);

  public static final DocumentField NESTED_VARIABLE_SURVEY_PERIOD_FIELD =
      new DocumentField(VariableDocument.VARIABLE_SURVEY_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + VariableSurvey.SURVEY_PERIOD_FIELD);

  // Nested: Variable Document - Variable Survey - Survey Period
  public static final DocumentField NESTED_VARIABLE_SURVEY_NESTED_PERIOD_START_DATE =
      new DocumentField(NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + DateRange.STARTDATE_FIELD);

  public static final DocumentField NESTED_VARIABLE_SURVEY_NESTED_PERIOD_END_DATE =
      new DocumentField(NESTED_VARIABLE_SURVEY_PERIOD_FIELD.getAbsolutePath()
          + DocumentField.PATH_DELIMITER + DateRange.ENDDATE_FIELD);

  // Nested: Variable Document - Answer Options
  public static final DocumentField NESTED_ANSWER_OPTIONS_CODE_FIELD =
      new DocumentField(ANSWER_OPTIONS_FIELD.getAbsolutePath() + DocumentField.PATH_DELIMITER
          + AnswerOption.CODE_FIELD);

  public static final DocumentField NESTED_ANSWER_OPTIONS_LABEL_FIELD =
      new DocumentField(ANSWER_OPTIONS_FIELD.getAbsolutePath() + DocumentField.PATH_DELIMITER
          + AnswerOption.LABEL_FIELD);

  /**
   * This is a nested reference to the survey.
   */
  @Valid
  @NotNull
  private VariableSurvey variableSurvey;

  /**
   * The name of the variable.
   */
  @Size(max = 32, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String name;

  /**
   * The data type of the variable.
   */
  @ValidDataType(groups = {Create.class, Edit.class})
  private String dataType;

  /**
   * The label of the variable.
   */
  @Size(max = 80, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String label;

  /**
   * This field holds the questions of the variable.
   */
  @Size(max = 256, groups = {Create.class, Edit.class})
  @NotBlank(groups = {Create.class, Edit.class})
  private String question;

  /**
   * A optional scale level of the variable, if the variable is e.g. not a String.
   */
  /*
   * One more validation (must field if datatype is numeric. happens in the Validator
   * VariableDocumentValidator.
   */
  @ValidScaleLevel(groups = {Create.class, Edit.class})
  private String scaleLevel;

  /**
   * The value (answer options) with depending labels are represent in this nested field.
   */
  @Valid
  @UniqueAnswerCode(groups = {Create.class, Edit.class})
  private List<AnswerOption> answerOptions;

  /**
   * A list of all variables which are depending to this variable.
   */
  @Valid
  private List<RelatedVariable> relatedVariables;

  /**
   * Create a variableDocument with a empty AnswerOption.
   */
  public VariableDocument() {
    super();
    this.answerOptions = new ArrayList<>();
    this.variableSurvey = new VariableSurvey();
    this.relatedVariables = new ArrayList<>();
  }

  /**
   * Adds a answer Option to the list.
   * 
   * @param answerOption an AnswerOption
   * @return Feedback for successful adding
   */
  public boolean addAnswerOption(AnswerOption answerOption) {

    // ignore null options
    if (answerOption == null) {
      return false;
    }

    return this.answerOptions.add(answerOption);
  }

  /**
   * Removes a answer Option from the list.
   * 
   * @param index an the index of the AnswerOption
   * @return The deleted AnswerOption
   */
  public AnswerOption removeAnswerOption(int index) {

    // ignore wrong index
    if (index < 0 || index >= this.answerOptions.size()) {
      return null;
    }

    return this.answerOptions.remove(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("super", super.toString())
        .add("variableSurvey", variableSurvey).add("name", name).add("dataType", dataType)
        .add("label", label).add("question", question).add("scaleLevel", scaleLevel)
        .add("answerOptions", answerOptions).add("relatedVariables", relatedVariables).toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#hashCode()
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), variableSurvey, name, dataType, label, question,
        scaleLevel, answerOptions, relatedVariables);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * eu.dzhw.fdz.metadatamanagement.data.common.documents.AbstractDocument#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    if (object != null && getClass() == object.getClass()) {
      if (!super.equals(object)) {
        return false;
      }
      VariableDocument that = (VariableDocument) object;
      return Objects.equal(this.variableSurvey, that.variableSurvey)
          && Objects.equal(this.name, that.name) && Objects.equal(this.dataType, that.dataType)
          && Objects.equal(this.label, that.label) && Objects.equal(this.question, that.question)
          && Objects.equal(this.scaleLevel, that.scaleLevel)
          && Objects.equal(this.answerOptions, that.answerOptions)
          && Objects.equal(this.relatedVariables, that.relatedVariables);
    }
    return false;
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

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public List<RelatedVariable> getRelatedVariables() {
    return relatedVariables;
  }

  public void setRelatedVariables(List<RelatedVariable> relatedVariables) {
    this.relatedVariables = relatedVariables;
  }
}
