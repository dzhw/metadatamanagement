package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Metadata which will be stored with each attachment of a {@link Survey}.
 */
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SurveyAttachmentMetadata extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = 4120776761095347391L;

  /**
   * The id of the attachment. Holds the complete path which can be used to download the file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link Survey} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.survey-id.not-empty")
  private String surveyId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link Survey} of this attachment
   * belongs.
   *
   * Must not be empty.
   */
  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * A description for this attachment.
   *
   * It must be specified in at least one language and it must not contain more than 512 characters.
   */
  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.description.not-null")
  @I18nStringSize(max = StringLengths.MEDIUM, message =
      "survey-management.error.survey-attachment-metadata.description.i18n-string-size")
  @I18nStringNotEmpty(message = "survey-management.error.survey-attachment-metadata."
      + "description.i18n-string-not-empty")
  private I18nString description;

  /**
   * A title of this attachment in the attachments' language.
   *
   * Must not be empty and it must not contain more than 2048 characters.
   */
  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.title.not-null")
  @Size(max = StringLengths.LARGE, message =
      "survey-management.error.survey-attachment-metadata.title.string-size")
  private String title;

  /**
   * The language of the attachments content.
   *
   * Must not be empty and must be specified as ISO 639 language code.
   */
  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "survey-management.error.survey-attachment-metadata.language.not-supported")
  private String language;

  /**
   * The filename of the attachment.
   *
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "survey-management.error.survey-attachment-metadata.filename.not-empty")
  @Pattern(message =
      "survey-management.error.survey-attachment-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The number of the {@link Survey} to which this attachment belongs.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.survey-number.not-null")
  private Integer surveyNumber;

  /**
   * The index in the {@link Survey} of this attachment. Used for sorting the attachments of this
   * {@link Survey}.
   *
   * Must not be empty.
   */
  @NotNull(message =
      "survey-management.error.survey-attachment-metadata.index-in-survey.not-null")
  private Integer indexInSurvey;

  /**
   * The doi of the attachment.
   *
   * Must not contain more than 512 characters.
   *
   * Must match the pattern of a doi-url https://doi.org/{id}
   */
  @Size(max = StringLengths.MEDIUM, message = "attachment.error.doi.size")
  @Pattern(
      message = "survey-management.error.survey-attachment-metadata.filename.not-valid",
      regexp = Patterns.DOI)
  private String doi;

  /**
   * Generate the id of this attachment from the surveyId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/surveys/" + surveyId + "/attachments/" + fileName);
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
