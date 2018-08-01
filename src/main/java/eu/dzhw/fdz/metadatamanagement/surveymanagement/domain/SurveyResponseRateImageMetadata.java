package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Metadata which will be stored in GridFS with each response rate image for surveys.
 */
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class SurveyResponseRateImageMetadata extends AbstractRdcDomainObject {
  @Id
  private String id;

  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.survey-id.not-empty")
  private String surveyId;

  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.project-id.not-empty")
  private String dataAcquisitionProjectId;

  @NotNull(message =
      "survey-management.error.survey-response-rate-image-metadata.language.not-null")
  @ValidIsoLanguage(message =
      "survey-management.error.survey-response-rate-image-metadata.language.not-supported")
  private String language;

  @NotEmpty(message =
      "survey-management.error.survey-response-rate-image-metadata.filename.not-empty")
  @Pattern(message =
      "survey-management.survey-response-rate-image-metadata.filename.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  @NotNull(message =
      "survey-management.error.survey-response-rate-image-metadata.survey-number.not-null")
  private Integer surveyNumber;
  
  public void generateId() {
    // hack to satisfy javers
    this.id = "/public/files/surveys/" + surveyId + "/" + fileName;
  }
}
