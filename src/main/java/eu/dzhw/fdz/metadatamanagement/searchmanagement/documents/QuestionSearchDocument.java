package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import io.searchbox.annotations.JestId;

/**
 * Representation of an question which is stored in elasticsearch.
 */
public class QuestionSearchDocument {
  @JestId
  private String id;
  private String dataAcquisitionProjectId;
  private String number;
  private String questionText;
  private String instruction;
  private String introduction;
  private String type;
  private String additionalQuestionText;
  private String imageType;
  private String topic;
  private String instrumentId;
  private List<String> successors;
  private String surveyTitle;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public QuestionSearchDocument(Question question, ElasticsearchIndices index) {
    this.id = question.getId();
    this.number = question.getNumber();
    this.instrumentId = question.getInstrumentId();
    this.dataAcquisitionProjectId = question.getDataAcquisitionProjectId();
    this.imageType = question.getImageType().name();
    this.successors = question.getSuccessors();    
    createI18nAttributes(question, index);
  }
  
  private void createI18nAttributes(Question question, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        questionText = question.getQuestionText() != null ? question.getQuestionText()
          .getDe() : null;
        instruction = question.getInstruction() != null ? question.getInstruction()
          .getDe() : null;
        introduction = question.getIntroduction() != null ? question.getIntroduction()
          .getDe() : null;
        type = question.getType() != null ? question.getType()
          .getDe() : null;
        additionalQuestionText = question.getAdditionalQuestionText() != null ? question
          .getAdditionalQuestionText().getDe() : null;
        topic = question.getTopic() != null ? question.getTopic().getDe() : null;  
        break;
      case METADATA_EN:
        questionText = question.getQuestionText() != null ? question.getQuestionText()
            .getEn() : null;
        instruction = question.getInstruction() != null ? question.getInstruction()
            .getEn() : null;
        introduction = question.getIntroduction() != null ? question.getIntroduction()
          .getEn() : null;
        type = question.getType() != null ? question.getType()
          .getEn() : null;
        additionalQuestionText = question.getAdditionalQuestionText() != null ? question
          .getAdditionalQuestionText().getEn() : null;
        topic = question.getTopic() != null ? question.getTopic().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }
  
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

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
  
  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getInstruction() {
    return instruction;
  }

  public void setInstruction(String instruction) {
    this.instruction = instruction;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getAdditionalQuestionText() {
    return additionalQuestionText;
  }

  public void setAdditionalQuestionText(String additionalQuestionText) {
    this.additionalQuestionText = additionalQuestionText;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public List<String> getSuccessors() {
    return successors;
  }

  public void setSuccessors(List<String> successors) {
    this.successors = successors;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
  
  public String getSurveyTitle() {
    return surveyTitle;
  }

  public void setSurveyTitles(String surveyTitle) {
    this.surveyTitle = surveyTitle;
  }
}
