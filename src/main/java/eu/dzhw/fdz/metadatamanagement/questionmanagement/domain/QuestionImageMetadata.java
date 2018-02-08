package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.Resolution;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The metadata for question images. An question image display the question in one language with
 * one given resolution, how the user saw the question on his device.
 *
 * @author Daniel Katzberg
 *
 */
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionImageMetadata extends AbstractRdcDomainObject {

  @NotNull(
      message = "question-management.error.question-image-metadata.image-type.not-null")
  @ValidQuestionImageType(
      message = "question-management.error.question-image-metadata.image-type."
        + "valid-question-image-type")
  private ImageType imageType;

  @NotEmpty(
      message = "question-management.error.question-image-metadata.language.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.question-image-metadata.language.size")
  @ValidIsoLanguage(message =
      "question-management.error.question-image-metadata.language.not-supported")
  private String language;

  @Valid
  private Resolution resolution;

  @NotEmpty(
      message = "question-management.error.question-image-metadata.file-name.not-empty")
  @Pattern(message = "question-management.error.question-image-metadata.file-name.not-valid",
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT)
  private String fileName;

  @NotNull(message = "question-management.error.question-image-metadata"
        + ".contains-annotations.not-null")
  private Boolean containsAnnotations;

  @NotNull(message = "question-management.error.question-image-metadata"
      + ".index-in-question.not-null")
  private Integer indexInQuestion;

  /* Foreign Keys */
  @NotEmpty(message = "question-management.error.question-image-metadata"
      + ".question-id.not-empty")
  private String questionId;

  @NotEmpty(message = "question-management.error.question-image-metadata"
       + ".data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;

  @Override
  public String getId() {
    return "/public/files/questions/" + questionId + "/images/" + fileName;
  }
}
