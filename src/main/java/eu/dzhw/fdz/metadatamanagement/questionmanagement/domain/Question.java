package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidShadowId;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation.ConceptExists;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderedStudy;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionIdName;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidQuestionType;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation.ValidUniqueQuestionNumber;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * A question is part of an {@link Instrument} which has been used in at least one {@link Survey}s.
 * The responses to a question are stored in {@link Variable}s.
 */
@Document(collection = "questions")
@CompoundIndex(def = "{instrumentId: 1, number: 1}", unique = true)
@ValidUniqueQuestionNumber(
    message = "question-management.error" + ".question.unique-question-number")
@ValidQuestionIdName(message = "question-management.error" + ".question.valid-question-id-name")
@EqualsAndHashCode(callSuper = false, of = "id")
@ToString(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValidShadowId(message = "question-management.error.question.id.pattern")
public class Question extends AbstractShadowableRdcDomainObject {

  private static final long serialVersionUID = -415874241616502840L;

  /**
   * The id of the question which uniquely identifies the question in this application.
   */
  @Id
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  @Setter(AccessLevel.NONE)
  private String id;

  /**
   * The master id of the question. It must not be empty, must be of the form
   * {@code que-{{dataAcquisitionProjectId}}-ins{{instrumentNumber}}-{{number}}$} and must not
   * contain more than 512 characters.
   */
  @NotEmpty(message = "question-management.error.question.master-id.not-empty")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.master-id.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_DOLLAR,
      message = "question-management.error.question.master-id.pattern")
  @Setter(AccessLevel.NONE)
  @Indexed
  private String masterId;

  /**
   * The id of the {@link DataAcquisitionProject} to which this question belongs.
   * 
   * The dataAcquisitionProjectId must not be empty.
   */
  @Indexed
  @NotEmpty(message = "question-management.error.question.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;

  /**
   * The number of the question.
   * 
   * Must not be empty and must be unique within the {@link Instrument}. Must contain only (german)
   * alphanumeric characters and "_","-" and "." and must not contain more than 32 characters.
   */
  @NotEmpty(message = "question-management.error.question.number.not-empty")
  @Size(max = StringLengths.SMALL, message = "question-management.error.question.number.size")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT,
      message = "question-management.error.question.number.pattern")
  private String number;

  /**
   * The question the {@link Survey}s participant was asked.
   * 
   * It must be specified in at least one language and it must not contain more than 2048
   * characters.
   */
  @NotNull(message = "question-management.error.question.question-text.not-null")
  @I18nStringNotEmpty(
      message = "question-management.error.question.question-text.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.question-text.i18n-string-size")
  private I18nString questionText;

  /**
   * The topic or section in the {@link Instrument} to which this question belongs.
   * 
   * It must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.topic.i18n-string-size")
  private I18nString topic;

  /**
   * The index of the question in the {@link Instrument}. Used for sorting the questions.
   */
  @NotNull(message = "question-management.error.question.indexInInstrument.not-null")
  private Integer indexInInstrument;

  /**
   * The id of the {@link Instrument} to which this question belongs.
   * 
   * Must not be empty.
   */
  @NotEmpty
  @Indexed
  private String instrumentId;

  /**
   * The number of the {@link Instrument} to which this question belongs.
   * 
   * Must not be empty.
   */
  @NotNull(message = "question-management.error.question.instrument-number.not-null")
  private Integer instrumentNumber;

  /**
   * The instruction for the participant which tells how to give the answers to this question.
   * 
   * Must not contain more than 1 MB characters.
   */
  @I18nStringSize(max = StringLengths.X_LARGE,
      message = "question-management.error.question.instruction.i18n-string-size")
  private I18nString instruction;

  /**
   * The introduction of this question which gives more context to the participant before asking the
   * question.
   * 
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.introduction.i18n-string-size")
  private I18nString introduction;

  /**
   * The type of the question.
   * 
   * Must be one of QuestionTypes and must not be empty.
   */
  @NotNull(message = "question-management.error.question.type.not-null")
  @ValidQuestionType(message = "question-management.error.question.type.valid-question-type")
  private I18nString type;

  /**
   * Arbitrary additional question text which has been presented to the participant.
   * 
   * Must not contain more than 1 MB characters.
   */
  @I18nStringSize(max = StringLengths.X_LARGE,
      message = "question-management.error.question.additional-question-text.i18n-string-size")
  private I18nString additionalQuestionText;

  /**
   * Arbitrary annotations to this question. Markdown is supported.
   * 
   * Must not contain more than 2048 characters.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.variable.annotations.i18n-string-size")
  private I18nString annotations;

  /**
   * A {@link TechnicalRepresentation} of this question. This is optional and can be used to add the
   * source code of the question which was used to generate it.
   */
  @Valid
  private TechnicalRepresentation technicalRepresentation;

  /**
   * List of numbers of the {@link Question}s which directly follow this question in the
   * {@link Instrument}.
   */
  private List<String> successorNumbers;

  /**
   * List of ids of the {@link Question}s which directly follow this question in the
   * {@link Instrument}.
   */
  @Indexed
  private List<String> successors;

  /**
   * The id of the {@link OrderedStudy} to which this question belongs.
   * 
   * Must not be empty.
   */
  @Indexed
  @NotEmpty(message = "question-management.error.question.study-id.not-empty")
  private String studyId;

  /**
   * List of ids of {@link Concept}s to which this question belongs.
   */
  @Indexed
  private List<@ConceptExists(
      message = "question-management.error.question.concept-ids.not-exists") String> conceptIds;

  public Question(Question question) {
    super();
    BeanUtils.copyProperties(question, this);
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
