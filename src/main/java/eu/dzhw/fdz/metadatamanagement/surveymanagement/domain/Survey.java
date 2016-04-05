package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.Period;
import eu.dzhw.fdz.metadatamanagement.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidSurveyIdName;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A Survey.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "surveys")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.builders")
@ValidSurveyIdName(message = "{error.survey.id.validSurveyIdName}")
public class Survey extends AbstractRdcDomainObject {

  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "{error.survey.id.notEmpty}")
  @Size(max = StringLengths.MEDIUM, message = "{error.survey.id.size}")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS,
      message = "{error.survey.id.pattern}")
  private String id;

  @I18nStringSize(max = StringLengths.MEDIUM, message = "{error.survey.title.i18nStringSize}")
  private I18nString title;

  @NotNull(message = "{error.survey.fieldPeriod.notNull}")
  @Valid
  private Period fieldPeriod;

  /* Foreign Keys */
  @NotEmpty(message = "{error.survey.dataAcquisitionProject.id.notEmpty}")
  private String dataAcquisitionProjectId;

  @NotEmpty(message = "{error.survey.questionnaire.id.notEmpty}")
  private String questionnaireId;
  
  private List<String> dataSetIds;

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  /*
   * (non-Javadoc)
   * 
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("title", title)
      .add("fieldPeriod", fieldPeriod)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .toString();
  }

  /* GETTER / SETTER */
  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public Period getFieldPeriod() {
    return fieldPeriod;
  }

  public void setFieldPeriod(Period fieldPeriod) {
    this.fieldPeriod = fieldPeriod;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getQuestionnaireId() {
    return questionnaireId;
  }

  public void setQuestionnaireId(String questionnaireId) {
    this.questionnaireId = questionnaireId;
  }

  public List<String> getDataSetIds() {
    return dataSetIds;
  }

  public void setDataSetIds(List<String> dataSetIds) {
    this.dataSetIds = dataSetIds;
  }
}
