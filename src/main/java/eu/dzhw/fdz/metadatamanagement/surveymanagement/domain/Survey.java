package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidDataType;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidSurveyIdName;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidUniqueSurveyNumber;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A Survey.
 *
 * @author Daniel Katzberg
 */
@Document(collection = "surveys")
@ValidSurveyIdName(message = "survey-management.error.survey.id.valid-survey-id-name")
@ValidUniqueSurveyNumber(message = "survey-management.error"
    + ".survey.unique-survey-number")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Survey extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "survey-management.error.survey.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "survey-management.error.survey.id.size")
  @Pattern(
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_DOLLAR,
      message = "survey-management.error.survey.id.pattern")
  private String id;

  @Indexed
  @NotEmpty(message = "survey-management.error.survey.data-acquisition-project.id.not-empty")
  private String dataAcquisitionProjectId;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.survey.title.i18n-string-size")
  private I18nString title;

  @NotNull(message = "survey-management.error.survey.population.not-null")  
  private Population population;

  @NotNull(message = "survey-management.error.survey.survey-method.not-null")
  @I18nStringNotEmpty(
      message = "survey-management.error.survey.survey-method.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.survey.survey-method.i18n-string-size")
  private I18nString surveyMethod;

  @NotNull(message = "survey-management.error.survey.number.not-null")
  private Integer number;

  @NotNull(message = "survey-management.error.survey.field-period.not-null")
  @Valid
  private Period fieldPeriod;

  @NotNull(message = "survey-management.error.survey.sample.not-null")
  @I18nStringNotEmpty(message = "survey-management.error.survey.sample.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.survey.sample.i18n-string-size")
  private I18nString sample;
  
  @NotNull(message = "survey-management.error.survey.wave.not-null")
  private Integer wave;

  private Integer grossSampleSize;

  @NotNull(message = "survey-management.error.survey.sample-size.not-null")
  private Integer sampleSize;

  private Double responseRate;
  
  @NotNull(message = "survey-management.error.survey.data-type.not-null")
  @ValidDataType(message = "survey-management.error.survey.data-type.valid-data-type")
  private I18nString dataType;

  @Indexed
  @NotEmpty(message = "survey-management.error.survey.study-id.not-empty")
  private String studyId;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  public Survey(Survey survey) {
    super();
    BeanUtils.copyProperties(survey, this);
  }
}
