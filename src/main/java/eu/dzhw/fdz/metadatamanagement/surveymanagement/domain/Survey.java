package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.javers.core.metamodel.annotation.Entity;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidSampleType;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidSurveyIdName;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidUniqueSurveyNumber;
import io.searchbox.annotations.JestId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A survey is conducted to examine a population on the basis of a sample. The resulting
 * {@link DataSet}s can be used to make statements about the population.
 */
@Entity
@Document(collection = "surveys")
@ValidSurveyIdName(message = "survey-management.error.survey.id.valid-survey-id-name")
@ValidUniqueSurveyNumber(message = "survey-management.error"
    + ".survey.unique-survey-number")
@ValidShadowId(message = "survey-management.error.survey.id.pattern")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Survey extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = -4940386697318232867L;

  /**
   * The id of the survey which uniquely identifies the survey in this application.
   */
  @Id
  @JestId
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the survey. It must not be empty, must be of the form
   * {@code sur-{{dataAcquisitionProjectId}}-sy{{number}}$} and must not contain more than 512
   * characters.
   */
  @NotEmpty(message = "survey-management.error.survey.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "survey-management.error.survey.master-id.size")
  @Pattern(
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_DOLLAR,
      message = "survey-management.error.survey.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The id of the {@link DataAcquisitionProject} to which this survey belongs.
   * 
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "survey-management.error.survey.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The title of the instrument.
   * 
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.survey.title.i18n-string-size")
  @I18nStringEntireNotEmpty(
      message = "survey-management.error.survey.title.i18n-string-entire-not-empty")
  private I18nString title;

  /**
   * Details about the {@link Population}.
   * 
   * Must not be empty.
   */
  @Valid
  @NotNull(message = "survey-management.error.survey.population.not-null")
  private Population population;

  /**
   * The survey method briefly describes how the data were collected.
   * 
   * It must be specified in German and English and it must not contain more than 2048 characters.
   */
  @NotNull(message = "survey-management.error.survey.survey-method.not-null")
  @I18nStringEntireNotEmpty(
      message = "survey-management.error.survey.survey-method.i18n-string-entire-not-empty")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.survey.survey-method.i18n-string-size")
  private I18nString surveyMethod;

  /**
   * The number of the instrument.
   * 
   * Must not be empty and must be unique within the {@link DataAcquisitionProject}.
   */
  @NotNull(message = "survey-management.error.survey.number.not-null")
  private Integer number;

  /**
   * The period during which the survey has been conducted or is expected to be conducted.
   * 
   * Must not be empty.
   */
  @NotNull(message = "survey-management.error.survey.field-period.not-null")
  @Valid
  private Period fieldPeriod;

  /**
   * The sampling method is the procedure for selecting sample members from a population.
   * It must match the controlled vocabulary specified by VFDB.
   * @see <a href=https://mdr.iqb.hu-berlin.de/#/catalog/1d791cc7-6d8d-dd35-b1ef-0eec9c31bbb5">
   * Catalog: GNERD: Sampling Procedure Educational Research (Version 1.0)
   * </a>
   */
  @NotNull(message = "survey-management.error.survey.sample.not-null")
  @ValidSampleType(message = "survey-management.error.survey.sample.valid-sample-type")
  private I18nString sample;

  /**
   * Number of the wave which this {@link Survey} represents. Will be ignored if the {@link Study}
   * is not organized in waves.
   * 
   * Must not be empty and must be greater than or equal to 1.
   */
  @NotNull(message = "survey-management.error.survey.wave.not-null")
  @Min(value = 1, message = "survey-management.error.survey.wave.min")
  private Integer wave;

  /**
   * The gross sample size represents the number of participants which have been invited to take
   * part in the {@link Survey}.
   * 
   * Must not be negative.
   */
  @Min(value = 0, message = "survey-management.error.survey.gross-sample-size.min")
  private Integer grossSampleSize;

  /**
   * The sample size is the number of participant which took part in the survey.
   * 
   * Must not be empty and must not be negative.
   */
  @NotNull(message = "survey-management.error.survey.sample-size.not-null")
  @Min(value = 0, message = "survey-management.error.survey.sample-size.min")
  private Integer sampleSize;

  /**
   * The response rate is the quotient of the gross sample size and the sample size.
   * 
   * Must be between 0 and 100.
   */
  @Min(value = 0, message = "survey-management.error.survey.response-rate.min")
  @Max(value = 100, message = "survey-management.error.survey.response-rate.max")
  private Double responseRate;

  /**
   * The type of data which the survey produced.
   * 
   * Must be one of {@link DataTypes} and must not be empty.
   */
  @NotNull(message = "survey-management.error.survey.data-type.not-null")
  @ValidDataType(message = "survey-management.error.survey.data-type.valid-data-type")
  private I18nString dataType;

  /**
   * The id of the {@link Study} to which this survey belongs.
   * 
   * Must not be empty.
   */
  @Indexed
  @NotEmpty(message = "survey-management.error.survey.study-id.not-empty")
  private String studyId;

  /**
   * Arbitrary additional text for this survey.
   * 
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  public Survey(Survey survey) {
    super();
    BeanUtils.copyProperties(survey, this);
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
