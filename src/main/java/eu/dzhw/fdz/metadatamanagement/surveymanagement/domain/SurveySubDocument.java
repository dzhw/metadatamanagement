package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import io.searchbox.annotations.JestId;

/**
 * Subset of survey attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class SurveySubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "survey-management.error.survey.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "survey-management.error.survey.id.size")
  @Pattern(
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_EXCLAMATIONMARK,
      message = "survey-management.error.survey.id.pattern")
  protected String id;
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.survey.title.i18n-string-size")
  protected I18nString title;
  @NotNull(message = "survey-management.error.survey.population.not-null")
  @I18nStringNotEmpty(message = "survey-management.error.survey.population.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.survey.population.i18n-string-size")
  protected I18nString population;
  @NotNull(message = "survey-management.error.survey.survey-method.not-null")
  @I18nStringNotEmpty(
      message = "survey-management.error.survey.survey-method.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.survey.survey-method.i18n-string-size")
  protected I18nString surveyMethod;
  @NotNull(message = "survey-management.error.survey.number.not-null")
  protected Integer number;

  public SurveySubDocument() {
    super();
  }
  
  public SurveySubDocument(Survey survey) {
    super();
    BeanUtils.copyProperties(survey, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public void setId(String id) {
    this.id = id;
  }

  public I18nString getPopulation() {
    return population;
  }

  public void setPopulation(I18nString population) {
    this.population = population;
  }

  public I18nString getSurveyMethod() {
    return surveyMethod;
  }

  public void setSurveyMethod(I18nString surveyMethod) {
    this.surveyMethod = surveyMethod;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }

}
