package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

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
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Question.
 * 
 * @author Daniel Katzberg
 */
@Document(collection = "questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders")
@CompoundIndex(def = "{instrumentId: 1, number: 1}", unique = true)
@ValidUniqueQuestionNumber(message = "question-management.error"
    + ".question.unique-question-number")
@ValidQuestionIdName(message = "question-management.error"
    + ".question.valid-question-id-name")
public class Question extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Pattern(regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT,
      message = "question-management.error.question.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  private String id;
  
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
      message = "question-management.error.question.topic.i18n-string-size")
  private I18nString topic;
  
  @Valid
  private TechnicalRepresentation technicalRepresentation;
  
  @NotNull(message = "question-management.error.question.instrument-number.not-null")
  private Integer instrumentNumber;
  
  private List<String> successorNumbers;
  
  private List<String> successors;
  
      
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "question-management.error.question.data-acquisition-project-id.not-empty")
  private String dataAcquisitionProjectId;
    
  private String instrumentId;
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#getId()
   */
  @Override
  public String getId() {
    return id;
  }    
  
  /*
   * (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("super", super.toString())
      .add("id", id)
      .add("number", number)
      .add("questionText", questionText)
      .add("instruction", instruction)
      .add("introduction", introduction)
      .add("type", type)
      .add("additionalQuestionText", additionalQuestionText)
      .add("imageType", imageType)
      .add("topic", topic)
      .add("technicalRepresentation", technicalRepresentation)
      .add("successors", successors)
      .add("dataAcquisitionProjectId", dataAcquisitionProjectId)
      .add("instrumentId", instrumentId)
      .add("instrumentNumber", instrumentNumber)
      .add("successorNumbers", successorNumbers)
      .toString();
  }

  /* GETTER / SETTER */
  public void setId(String id) {
    this.id = id;
  }
  
  public String getNumber() {
    return number;
  }
  
  public void setNumber(String number) {
    this.number = number;
  }
  
  public I18nString getQuestionText() {
    return questionText;
  }
  
  public void setQuestionText(I18nString questionText) {
    this.questionText = questionText;
  }
  
  public I18nString getInstruction() {
    return instruction;
  }
  
  public void setInstruction(I18nString instruction) {
    this.instruction = instruction;
  }
  
  public I18nString getIntroduction() {
    return introduction;
  }
  
  public void setIntroduction(I18nString introduction) {
    this.introduction = introduction;
  }
  
  public I18nString getType() {
    return type;
  }
  
  public void setType(I18nString type) {
    this.type = type;
  }
  
  public I18nString getAdditionalQuestionText() {
    return additionalQuestionText;
  }
  
  public void setAdditionalQuestionText(I18nString additionalQuestionText) {
    this.additionalQuestionText = additionalQuestionText;
  }
    
  public ImageType getImageType() {
    return imageType;
  }

  public void setImageType(ImageType imageType) {
    this.imageType = imageType;
  }

  public TechnicalRepresentation getTechnicalRepresentation() {
    return technicalRepresentation;
  }

  public void setTechnicalRepresentation(TechnicalRepresentation technicalRepresentation) {
    this.technicalRepresentation = technicalRepresentation;
  }

  public List<String> getSuccessors() {
    return successors;
  }

  public void setSuccessors(List<String> successors) {
    this.successors = successors;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }
  
  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public I18nString getTopic() {
    return topic;
  }

  public void setTopic(I18nString topic) {
    this.topic = topic;
  }

  public Integer getInstrumentNumber() {
    return instrumentNumber;
  }

  public void setInstrumentNumber(Integer instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
  }

  public List<String> getSuccessorNumbers() {
    return successorNumbers;
  }

  public void setSuccessorNumbers(List<String> successorNumbers) {
    this.successorNumbers = successorNumbers;
  }
}
