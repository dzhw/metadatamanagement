package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.RestrictedScaleLevelForDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsFirstQuartileMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMaximumMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMaximumMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMedianMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMedianMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMinimumMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsMinimumMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsThirdQuartileMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueVariableNameInDataSet;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidAccessWays;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidDerivedVariablesIdentifier;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidRepeatedMeasurementIdentifier;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidScaleLevel;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidStorageType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidVariableIdName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A variable contains the results from at least one {@link Survey}. These results can be the
 * responses from participants of an online survey, hence a variable can result from
 * {@link RelatedQuestion}s. A variable is part of exactly one {@link DataSet}.
 */
@Document(collection = "variables")
@CompoundIndexes({@CompoundIndex(def = "{name: 1, dataSetId: 1}", unique = true),
    @CompoundIndex(def = "{indexInDataSet: 1, dataSetId: 1}", unique = false)})
@ValidShadowId(message = "variable-management.error.variable.id.pattern")
@ValidVariableIdName(message = "variable-management.error.variable.valid-variable-name")
@ValidRepeatedMeasurementIdentifier(
    message = "variable-management.error.variable.valid-repeated-measurement-identifier")
@ValidDerivedVariablesIdentifier(
    message = "variable-management.error.variable.valid-derived-variables-identifier")
@UniqueVariableNameInDataSet(
    message = "variable-management.error." + "variable.unique-variable-name-in-data-set")
@RestrictedScaleLevelForDateDataType(
    message = "variable-management.error.variable." + "restricted-scale-level-for-date-data-type")

// Validation if data type is date
@ValidResponseValueMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "valid-response-value-must-be-an-iso-date-on-date-data-type")
@StatisticsMinimumMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-minimum-must-be-an-iso-date-on-date-data-type")
@StatisticsMaximumMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-maximum-must-be-an-iso-date-on-date-data-type")
@StatisticsMedianMustBeAnIsoDateOnDateDataType(message = "variable-management.error.variable."
    + "statistics-median-must-be-an-iso-date-on-date-data-type")
@StatisticsFirstQuartileMustBeAnIsoDateOnDateDataType(message = "variable-management.error."
    + "variable.statistics-first-quartile-must-be-an-iso-date-on-date-data-type")
@StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType(message = "variable-management.error."
    + "variable.statistics-third-quartile-must-be-an-iso-date-on-date-data-type")

// Validation if data type is numeric
@ValidResponseValueMustBeANumberOnNumericDataType(message = "variable-management.error.variable."
    + "valid-response-value-must-be-a-number-on-numeric-data-type")
@StatisticsMinimumMustBeANumberOnNumericDataType(message = "variable-management.error.variable."
    + "statistics-minimum-must-be-a-number-on-numeric-data-type")
@StatisticsMaximumMustBeANumberOnNumericDataType(message = "variable-management.error.variable."
    + "statistics-maximum-must-be-a-number-on-numeric-data-type")
@StatisticsMedianMustBeANumberOnNumericDataType(message = "variable-management.error.variable."
    + "statistics-median-must-be-a-number-on-numeric-data-type")
@StatisticsFirstQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-first-quartile-must-be-a-number-on-numeric-data-type")
@StatisticsThirdQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-third-quartile-must-be-a-number-on-numeric-data-type")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Variable extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = -2425155869960913542L;

  /**
   * The id of the variable which uniquely identifies the variable in this application.
   *
   * The id must not be empty and must be of the form
   * var-{{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{name}}$. The id must not contain more
   * than 512 characters.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  @NotEmpty(message = "variable-management.error.variable.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "variable-management.error.variable.master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "variable-management.error.variable.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The PID of the variable.
   *
   * Must not contain more than 512 characters.
   *
   * Must match the pattern of a PID "21.T11998/dzhw:{DataPackageID}_{VariableID}:{version}
   */
  @Size(max = StringLengths.MEDIUM, message = "variable-management.error.pid.size")
  @Pattern(
      message = "variable-management.error.pid.pattern",
      regexp = Patterns.PID)
  private String pid;

  /**
   * The id of the {@link DataAcquisitionProject} to which this variable belongs.
   *
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "variable-management.error.variable.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The name of the variable as it is used in the {@link DataSet}.
   *
   * It must not be empty and must be unique in the {@link DataSet}. It must contain only
   * alphanumeric (english) characters and "_". The first character must not be a number. It must
   * not contain more than 32 characters.
   */
  @NotEmpty(message = "variable-management.error.variable.name.not-empty")
  @Size(max = StringLengths.SMALL, message = "variable-management.error.variable.name.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "variable-management.error.variable.name.pattern")
  private String name;

  /**
   * The label of the variable should describe its content.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message = "variable-management.error.variable.label.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.label.i18n-string-size")
  @I18nStringNotEmpty(message = "variable-management.error.variable.label.i18n-string-not-empty")
  private I18nString label;

  /**
   * Arbitrary additional text for this variable. Markdown is supported.
   *
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * The id of the {@link DataSet} to which this variable belongs.
   *
   * Must not be empty.
   */
  @Indexed
  @NotEmpty(message = "variable-management.error.variable.data-set-id-not-empty")
  private String dataSetId;

  /**
   * The number of the {@link DataSet} to which this variable belongs.
   *
   * Must not be empty.
   */
  @NotNull(message = "variable-management.error.variable.data-set-number-not-null")
  private Integer dataSetNumber;

  /**
   * The technical type which the {@link ValidResponse}s have.
   *
   * Must be one of {@link DataTypes} and must not be empty.
   */
  @NotNull(message = "variable-management.error.variable.data-type.not-null")
  @ValidDataType(message = "variable-management.error.variable.data-type.valid-data-type")
  private I18nString dataType;

  /**
   * Associated with each data type is a storage type. For instance numerics can be stored as
   * integer or double.
   *
   * Must be one of {@link StorageTypes} and must not be empty.
   */
  @NotNull(message = "variable-management.error.variable.storage-type.not-null")
  @ValidStorageType(message = "variable-management.error.variable.storage-type.valid-storage-type")
  private String storageType;

  /**
   * The scale level (or level of measurement) classifies the nature of information within the
   * values assigned to this variable ({@link ValidResponse}s). It determines which mathematical
   * operations can be performed with the values.
   *
   * It must be one of {@link ScaleLevels} and must not be empty. If the data type of this variable
   * is {@link DataTypes#DATE} then the ScaleLevel must be {@link ScaleLevels#ORDINAL},
   * {@link ScaleLevels#INTERVAL} or {@link ScaleLevels#NOMINAL}.
   */
  @NotNull(message = "variable-management.error.variable.scaleLevel.not-null")
  @ValidScaleLevel(message = "variable-management.error.variable.scaleLevel.valid-scale-level")
  private I18nString scaleLevel;

  /**
   * The access way of this variable. Depends on the sensitivity of the data and describes how the
   * data user will be able to work with the data.
   *
   * Must not be empty and be one of {@link AccessWays}.
   */
  // checks for min size too.
  @NotEmpty(message = "variable-management.error.variable.access-ways.not-empty")
  @ValidAccessWays(message = "variable-management.error.variable.access-ways.valid-access-ways")
  private List<String> accessWays;

  /**
   * List of ids of variables which are "related" to this variable. The type of relation is
   * arbitrary.
   */
  private List<String> relatedVariables;

  /**
   * The index in the {@link DataSet} of this variable. Used for sorting the variables of this
   * {@link DataSet} and for displaying successors and predecessors of this variable.
   *
   * Must not be empty and the successor of this variable must have indexInDataSet incremented by
   * one.
   */
  @NotNull(message = "variable-management.error.variable.data-set-index-not-null")
  private Integer indexInDataSet;

  /**
   * List of numbers of {@link Survey}s which have been conducted to create this variable.
   *
   * Must not be empty.
   */
  @NotEmpty(message = "variable-management.error.variable.survey-numbers-not-empty")
  private List<Integer> surveyNumbers;

  /**
   * Identifier used to group variables within this {@link DataSet} which measure the same across
   * multiple waves.
   *
   * Must be of the form {{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{string}}$. Must not
   * contain more than 512 characters and must contain only (german) alphanumeric characters and "_"
   * and "-".
   */
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.repeated-measurement-identifier-size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable-management.error.variable.repeated-measurement-pattern")
  private String repeatedMeasurementIdentifier;

  /**
   * Identifier used to group variables within this {@link DataSet} which have been derived from
   * each other. For instance one variable might be an aggregated version of the other.
   *
   * Must be of the form {{dataAcquisitionProjectId}}-ds{{dataSetNumber}}-{{string}}$. Must not
   * contain more than 512 characters and must contain only (german) alphanumeric characters and "_"
   * and "-".
   */
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.derived-variables-identifier-size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable-management.error.variable.derived-variables-identifier-pattern")
  private String derivedVariablesIdentifier;

  /**
   * {@link FilterDetails} of a variable describe the condition which must have evaluated to true
   * before a participant was asked a {@link Question} resulting in this variable.
   */
  @Valid
  private FilterDetails filterDetails;

  /**
   * {@link GenerationDetails} describe how this variable was generated from one or more input
   * variables.
   */
  @Valid
  private GenerationDetails generationDetails;

  /**
   * The {@link Distribution} contains the descriptives of this variable meaning
   * {@link ValidResponse}s, {@link Missing}s and {@link Statistics}.
   */
  @Valid
  private Distribution distribution;

  /**
   * List of {@link RelatedQuestion}s which have been asked to generate the values of this variable.
   */
  @Valid
  private List<RelatedQuestion> relatedQuestions;

  /**
   * Flag indicating whether the {@link ValidResponse}s should be displayed with a thousands
   * separator or not. For instance years (1970) are numeric but should not be displayed with a
   * thousands separator. Default value is false indicating that the {@link ValidResponse}s are
   * displayed with thousands separator.
   */
  @Builder.Default
  private Boolean doNotDisplayThousandsSeparator = false;

  /**
   * Id of the {@link DataPackage} to which this variable belongs.
   */
  @Indexed
  @NotEmpty(message = "variable-management.error.variable.dataPackage-id.not-empty")
  private String dataPackageId;

  /**
   * List of ids of {@link Survey}s which have been conducted to create this variable.
   *
   * Must not be empty.
   */
  @Indexed
  private List<String> surveyIds;

  public Variable(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
  }

  @Override
  protected void setMasterIdInternal(String masterId) {
    this.masterId = masterId;
  }

  @Override
  protected void setIdInternal(String id) {
    this.id = id;
  }
}
