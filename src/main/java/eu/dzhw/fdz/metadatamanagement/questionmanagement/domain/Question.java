package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionIdName;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionImageType;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionType;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidUniqueQuestionNumber;
import io.searchbox.annotations.JestId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Question.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "questions")
@CompoundIndex(def = "{instrumentId: 1, number: 1}", unique = true)
@ValidUniqueQuestionNumber(message = "question-management.error"
    + ".question.unique-question-number")
@ValidQuestionIdName(message = "question-management.error"
    + ".question.valid-question-id-name")
@Data
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Question extends AbstractRdcDomainObject {
  
  @Id
  @JestId
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Pattern(
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_DOLLAR,
      message = "question-management.error.question.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  private String id;
  
  @Indexed
  @NotEmpty(message = "question-management.error.question.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;
  
  @NotEmpty(message = "question-management.error.question.number.not-empty")
  @Size(max = StringLengths.SMALL, message = "question-management.error.question.number.size")
  private String number;
  
  @NotNull(message = "question-management.error.question.question-text.not-null")
  @I18nStringNotEmpty(
      message = "question-management.error.question.question-text.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.question-text.i18n-string-size")
  private I18nString questionText;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.topic.i18n-string-size")
  private I18nString topic;
  
  @NotNull(message = "question-management.error.question.indexInInstrument.not-null")
  private Integer indexInInstrument;
  
  @NotEmpty
  @Indexed
  private String instrumentId;
  
  @NotNull(message = "question-management.error.question.instrument-number.not-null")
  private Integer instrumentNumber;

  @I18nStringSize(max = StringLengths.LARGE, 
      message = "question-management.error.question.instruction.i18n-string-size")
  private I18nString instruction;
  
  @I18nStringSize(max = StringLengths.LARGE, 
      message = "question-management.error.question.introduction.i18n-string-size")
  private I18nString introduction;
  
  @NotNull(message = "question-management.error.question.type.not-null")
  @ValidQuestionType(message = "question-management.error.question.type.valid-question-type")
  private I18nString type;
  
  @I18nStringSize(max = StringLengths.X_LARGE, 
      message = "question-management.error.question.additional-question-text.i18n-string-size")
  private I18nString additionalQuestionText;
  
  @NotNull(message = "question-management.error.question.image-type.not-null")
  @ValidQuestionImageType(
      message = "question-management.error.question.image-type.valid-question-image-type")
  private ImageType imageType;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;
  
  @Valid
  private TechnicalRepresentation technicalRepresentation;
  
  private List<String> successorNumbers;
  
  @Indexed
  private List<String> successors;
      
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "question-management.error.question.study-id.not-empty")
  private String studyId;

  public Question(Question question) {
    super();
    BeanUtils.copyProperties(question, this);
  }
}
