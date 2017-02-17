package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
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
public class Question extends QuestionSubDocument {
  
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
  
  @Valid
  private TechnicalRepresentation technicalRepresentation;
  
  private List<String> successorNumbers;
  
  private List<String> successors;
      
  /* Foreign Keys */
  @Indexed
  @NotEmpty(message = "question-management.error.question.study-id.not-empty")
  private String studyId;

  public Question() {
    super();
  }
  
  public Question(Question question) {
    super();
    BeanUtils.copyProperties(question, this);
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
      .add("studyId", studyId)
      .toString();
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

  public List<String> getSuccessorNumbers() {
    return successorNumbers;
  }

  public void setSuccessorNumbers(List<String> successorNumbers) {
    this.successorNumbers = successorNumbers;
  }

  public String getStudyId() {
    return studyId;
  }

  public void setStudyId(String studyId) {
    this.studyId = studyId;
  }  
}
