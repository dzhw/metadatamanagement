package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
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
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Metadata which will be stored with each response rate image of a {@link Survey}.
 */
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SurveyResponseRateImageMetadata extends AbstractShadowableRdcDomainObject {
  /**
   * The id of the response rate image. Holds the complete path which can be used to download the
   * file.
   */
  @Id
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the survey response rate image metadata.
   */
  @Setter(AccessLevel.NONE)
  private String masterId;

  /**
   * The id of the {@link Survey} to which this response rate image belongs.
   * 
   * Must not be empty.
   */
  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.survey-id.not-empty")
  private String surveyId;

  /**
   * The id of the {@link DataAcquisitionProject} to which the {@link Survey} of this response rate
   * image belongs.
   * 
   * Must not be empty.
   */
  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The language used in the response rate image.
   * 
   * Must be either "de" or "en".
   */
  @NotNull(message =
      "survey-management.error.survey-response-rate-image-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "survey-management.error.survey-response-rate-image-metadata.language.not-supported")
  private String language;

  /**
   * The filename of the image.
   * 
   * Must not be empty and must contain only (german) alphanumeric characters and "_" and "-" and
   * ".".
   */
  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.filename.not-empty")
  @Pattern(message =
      "survey-management.survey-response-rate-image-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * The number of the {@link Survey} to which this response rate image belongs.
   * 
   * Must not be empty.
   */
  @NotNull(message =
      "survey-management.error.survey-response-rate-image-metadata.survey-number.not-null")
  private Integer surveyNumber;
  
  /**
   * Generate the id of this image from the surveyId and the fileName.
   */
  public void generateId() {
    // hack to satisfy javers
    this.setId("/public/files/surveys/" + surveyId + "/" + fileName);
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
