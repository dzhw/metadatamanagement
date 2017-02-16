package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.util.Patterns;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import io.searchbox.annotations.JestId;

/**
 * Subset of question attributes which can be used in other search documents
 * as sub document.
 * @author René Reitmann
 */
public class QuestionSubDocument extends AbstractRdcDomainObject {

  @Id
  @JestId
  @NotEmpty(message = "question-management.error.question.id.not-empty")
  @Pattern(
      regexp = Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_EXCLAMATIONMARK,
      message = "question-management.error.question.id.pattern")
  @Size(max = StringLengths.MEDIUM, message = "question-management.error.question.id.size")
  protected String id;
  
  @Indexed
  @NotEmpty(message = "question-management.error.question.data-acquisition-project-id.not-empty")
  protected String dataAcquisitionProjectId;
  
  @NotEmpty(message = "question-management.error.question.number.not-empty")
  @Size(max = StringLengths.SMALL, message = "question-management.error.question.number.size")
  protected String number;
  
  @NotNull(message = "question-management.error.question.question-text.not-null")
  @I18nStringNotEmpty(
      message = "question-management.error.question.question-text.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.question-text.i18n-string-size")
  protected I18nString questionText;
  
  @I18nStringSize(max = StringLengths.LARGE,
      message = "question-management.error.question.topic.i18n-string-size")
  protected I18nString topic;
  
  protected String instrumentId;
  
  @NotNull(message = "question-management.error.question.instrument-number.not-null")
  protected Integer instrumentNumber;

  public QuestionSubDocument() {
    super();
  }
  
  public QuestionSubDocument(Question question) {
    super();
    BeanUtils.copyProperties(question, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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

  public Integer getInstrumentNumber() {
    return instrumentNumber;
  }

  public void setInstrumentNumber(Integer instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
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

  public I18nString getTopic() {
    return topic;
  }

  public void setTopic(I18nString topic) {
    this.topic = topic;
  }

}
