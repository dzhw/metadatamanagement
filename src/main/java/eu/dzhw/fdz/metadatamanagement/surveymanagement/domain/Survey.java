package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Period;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidSurveyIdName;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidUniqueSurveyNumber;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 *
 * @author Daniel Katzberg
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.builders")
@ValidSurveyIdName(message = "survey-management.error.survey.id.valid-survey-id-name")
@ValidUniqueSurveyNumber(message = "survey-management.error"
    + ".survey.unique-survey-number")
public class Survey extends SurveySubDocument {

  @NotNull(message = "survey-management.error.survey.field-period.not-null")
  @Valid
  private Period fieldPeriod;

  @NotNull(message = "survey-management.error.survey.sample.not-null")
  @I18nStringNotEmpty(message = "survey-management.error.survey.sample.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.survey.sample.i18n-string-size")
  private I18nString sample;

  @NotNull(message = "survey-management.error.survey.gross-sample-size.not-null")
  private Integer grossSampleSize;
  
  @NotNull(message = "survey-management.error.survey.sample-size.not-null")
  private Integer sampleSize;
  
  @NotNull(message = "survey-management.error.survey.response-rate.not-null")
  private Double responseRate;
  
  private String studyId;

  public Survey() {
    super();
  }
  
  public Survey(Survey survey) {
    super();
    BeanUtils.copyProperties(survey, this);
  }
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("title", title)
      .add("fieldPeriod", fieldPeriod)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("population", population)
      .add("sample", sample)
      .add("surveyMethod", surveyMethod)
      .add("grossSampleSize", grossSampleSize)
      .add("sampleSize", sampleSize)
      .add("responseRate", responseRate)
      .add("number", number)
      .add("studyId", studyId)
      .toString();
  }

  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }

  public I18nString getSample() {
    return sample;
  }

  public void setSample(I18nString sample) {
    this.sample = sample;
  }

  public Integer getGrossSampleSize() {
    return grossSampleSize;
  }


  public void setGrossSampleSize(Integer grossSampleSize) {
    this.grossSampleSize = grossSampleSize;
  }


  public Integer getSampleSize() {
    return sampleSize;
  }


  public void setSampleSize(Integer sampleSize) {
    this.sampleSize = sampleSize;
  }


  public Double getResponseRate() {
    return responseRate;
  }


  public void setResponseRate(Double responseRate) {
    this.responseRate = responseRate;
  }


  public String getStudyId() {
    return studyId;
  }


  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }  
}
