package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.Resolution;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionImageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The metadata for question images. An question image display the question in one language with
 * one given resolution, how the user saw the question on his device.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class QuestionImageMetadata {

  @NotNull(
      message = "question-management.error.question.question-image-metadata.image-type.not-null")
  @ValidQuestionImageType(
      message = "question-management.error.question.question-image-metadata.image-type."
        + "valid-question-image-type")
  private ImageType imageType;
  
  @NotEmpty(
      message = "question-management.error.question.question-image-metadata.language.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.question.question-image-metadata.language.size")
  private String language;
  
  @Valid
  private Resolution resolution;
  
  @NotEmpty(
      message = "question-management.error.question.question-image-metadata.file-name.not-empty")
  @Size(max = StringLengths.SMALL,
      message = "question-management.error.question.question-image-metadata.file-name.size")
  private String fileName;
  
  @NotNull(message = "question-management.error.question.question-image-metadata"
        + ".contains-annotations.not-null")
  private Boolean containsAnnotations;
  
  /* Foreign Keys */
  @NotEmpty(message = "question-management.error.question-image-metadatamanagement"
      + ".question-id.not-empty")
  private String questionId;
    
  @NotEmpty(message = "question-management.error.question-image-metadatamanagement"
       + ".data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;
  
}
