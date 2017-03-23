package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation.ValidIsoLanguage;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Metadata which will be stored in GridFS with each attachment for surveys.
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.builders")
public class SurveyAttachmentMetadata extends AbstractRdcDomainObject {
  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.survey-id.not-empty")
  private String surveyId;

  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "survey-management.error.survey-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "survey-management.error.survey-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.title.not-null")
  @Size(max = StringLengths.MEDIUM, message =
      "survey-management.error.survey-attachment-metadata.title.string-size")
  private String title;

  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "survey-management.error.survey-attachment-metadata.language.not-supported")
  private String language;

  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.filename.not-empty")
  private String fileName;

  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.survey-number.not-null")
  private Integer surveyNumber;

  @Override
  public String getId() {
    return "/public/files/surveys/" + surveyId + "/attachments/" + fileName;
  }

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Integer getSurveyNumber() {
    return surveyNumber;
  }

  public void setSurveyNumber(Integer surveyNumber) {
    this.surveyNumber = surveyNumber;
  }
}
