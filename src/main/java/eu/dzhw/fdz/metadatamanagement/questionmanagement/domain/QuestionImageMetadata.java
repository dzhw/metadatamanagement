package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.Resolution;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The metadata for one question images. One question image displays the question in one language
 * with one given resolution.
 */
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class QuestionImageMetadata extends AbstractRdcDomainObject {

  /**
   * The type of this image.
   * 
   * Must be one of {@link ImageType} and must not be empty.
   */
  @NotNull(
      message = "question-management.error.question-image-metadata.image-type.not-null")
  @ValidQuestionImageType(
      message = "question-management.error.question-image-metadata.image-type."
        + "valid-question-image-type")
  private ImageType imageType;

  /**
   * The language of the question text on this image.
   * 
   * Must not be empty and must be a valid ISO 639 code.
   */
  @NotEmpty(
      message = "question-management.error.question-image-metadata.language.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.question-image-metadata.language.size")
  @ValidIsoLanguage(message =
      "question-management.error.question-image-metadata.language.not-supported")
  private String language;

  /**
   * The resolution of the image.
   */
  @Valid
  private Resolution resolution;

  /**
   * The name of the images file.
   * 
   * Must not be empty and must only contain (german) alphanumeric characters and "_","-" and ".".
   */
  @NotEmpty(
      message = "question-management.error.question-image-metadata.file-name.not-empty")
  @Pattern(message = "question-management.error.question-image-metadata.file-name.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  /**
   * Flag indicating whether the image contains annotations which highlight parts that were only
   * visible to specific participants. These annotations were not visible to the participants.
   */
  @NotNull(message = "question-management.error.question-image-metadata"
        + ".contains-annotations.not-null")
  private Boolean containsAnnotations;

  /**
   * The index in the {@link Question} of this image. Used for sorting the images of this
   * {@link Question}.
   * 
   * Must not be empty.
   */
  @NotNull(message = "question-management.error.question-image-metadata"
      + ".index-in-question.not-null")
  private Integer indexInQuestion;

  /**
   * The id of the {@link Question} to which this image belongs.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "question-management.error.question-image-metadata"
      + ".question-id.not-empty")
  private String questionId;

  /**
   * The id of the {@link DataAcquisitionProject} of the {@link Question} to which this image
   * belongs.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "question-management.error.question-image-metadata"
       + ".data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;

  @Override
  public String getId() {
    return "/public/files/questions/" + questionId + "/images/" + fileName;
  }
}
