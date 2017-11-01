package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.OnlyOrdinalScaleLevelForDateDataType;
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
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidPanelIdentifier;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeANumberOnNumericDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidResponseValueMustBeAnIsoDateOnDateDataType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidScaleLevel;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidStorageType;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidVariableIdName;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

//validation for scalelevel ration
//TODO DKatzberg delete late: @MandatoryHistogramOnRatioScaleLevel(
//message = "variable-management.error.variable.histogram-must-be-set-on-ratio-scale-level")
/**
 * A Variable.
 *
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Document(collection = "variables")
@CompoundIndexes({
    @CompoundIndex(def = "{name: 1, dataSetId: 1}", unique = true),
    @CompoundIndex(def = "{indexInDataSet: 1, dataSetId: 1}", unique = false)
    })
@ValidVariableIdName(message = "variable-management.error.variable.valid-variable-name")
@ValidPanelIdentifier(message = "variable-management.error.variable.valid-panel-identifier")
@ValidDerivedVariablesIdentifier(message =
    "variable-management.error.variable.valid-derived-variables-identifier")
@UniqueVariableNameInDataSet(message = "variable-management.error."
    + "variable.unique-variable-name-in-data-set")
@OnlyOrdinalScaleLevelForDateDataType(
    message = "variable-management.error.variable.only-ordinal-scale-level-for-date-data-type")

//Validation if data type is date
@ValidResponseValueMustBeAnIsoDateOnDateDataType(
    message = "variable-management.error.variable."
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

//Validation if data type is numeric
@ValidResponseValueMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "valid-response-value-must-be-a-number-on-numeric-data-type")
@StatisticsMinimumMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-minimum-must-be-a-number-on-numeric-data-type")
@StatisticsMaximumMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-maximum-must-be-a-number-on-numeric-data-type")
@StatisticsMedianMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-median-must-be-a-number-on-numeric-data-type")
@StatisticsFirstQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-first-quartile-must-be-a-number-on-numeric-data-type")
@StatisticsThirdQuartileMustBeANumberOnNumericDataType(
    message = "variable-management.error.variable."
        + "statistics-third-quartile-must-be-a-number-on-numeric-data-type")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variable extends AbstractRdcDomainObject {
  @Id
  @JestId
  @NotEmpty(message = "variable-management.error.variable.id.not-empty")
  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR,
      message = "variable-management.error.variable.id.pattern")
  private String id;

  @Indexed
  @NotEmpty(message = "variable-management.error.variable.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "variable-management.error.variable.name.not-empty")
  @Size(max = StringLengths.SMALL, message = "variable-management.error.variable.name.size")
  @Pattern(regexp = Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN,
      message = "variable-management.error.variable.name.pattern")
  private String name;

  @NotNull(message = "variable-management.error.variable.label.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.label.i18n-string-size")
  @I18nStringNotEmpty(message = "variable-management.error.variable.label.i18n-string-not-empty")
  private I18nString label;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  @Indexed
  @NotEmpty(message = "variable-management.error.variable.data-set-id-not-empty")
  private String dataSetId;

  @NotNull(message = "variable-management.error.variable.data-set-number-not-null")
  private Integer dataSetNumber;

  @NotNull(message = "variable-management.error.variable.data-type.not-null")
  @ValidDataType(message = "variable-management.error.variable.data-type.valid-data-type")
  private I18nString dataType;

  @NotNull(message = "variable-management.error.variable.storage-type.not-null")
  @ValidStorageType(message = "variable-management.error.variable.storage-type.valid-storage-type")
  private String storageType;

  @NotNull(message = "variable-management.error.variable.scaleLevel.not-null")
  @ValidScaleLevel(
      message = "variable-management.error.variable.scaleLevel.valid-scale-level")
  private I18nString scaleLevel;

  // checks for min size too.
  @NotEmpty(message = "variable-management.error.variable.access-ways.not-empty")
  @ValidAccessWays(
      message = "variable-management.error.variable.access-ways.valid-access-ways")
  private List<String> accessWays;

  private List<String> relatedVariables;

  @NotNull(message = "variable-management.error.variable.data-set-index-not-null")
  private Integer indexInDataSet;

  @NotEmpty(message = "variable-management.error.variable.survey-numbers-not-empty")
  private List<Integer> surveyNumbers;

  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.panel-identifier-size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable-management.error.variable.panel-identifier-pattern")
  private String panelIdentifier;

  @Size(max = StringLengths.MEDIUM,
      message = "variable-management.error.variable.derived-variables-identifier-size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "variable-management.error.variable.derived-variables-identifier-pattern")
  private String derivedVariablesIdentifier;

  /* Nested Objects */
  @Valid
  private FilterDetails filterDetails;

  @Valid
  private GenerationDetails generationDetails;

  @Valid
  private Distribution distribution;

  @Valid
  private List<RelatedQuestion> relatedQuestions;

  private Boolean doNotDisplayThousandsSeparator = false;

  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "variable-management.error.variable.study-id.not-empty")
  private String studyId;

  @Indexed
  private List<String> surveyIds;

  public Variable(Variable variable) {
    super();
    BeanUtils.copyProperties(variable, this);
  }
}
