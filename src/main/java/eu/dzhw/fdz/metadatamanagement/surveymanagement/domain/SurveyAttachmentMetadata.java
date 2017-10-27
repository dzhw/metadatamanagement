package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored in GridFS with each attachment for surveys.
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
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
  @Size(max = StringLengths.LARGE, message =
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
  
  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.index-in-survey.not-null")
  private Integer indexInSurvey;

  @Override
  public String getId() {
    return "/public/files/surveys/" + surveyId + "/attachments/" + fileName;
  }
}
