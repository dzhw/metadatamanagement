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
  private String surveyId;
  private String questionText;
  private String instruction;
  private String introduction;
  private String type;
  private String additionalQuestionText;
  private String imageType;
  private String technicalRepresentation;
  private List<String> predecessor;
  private List<String> successor;
  private List<String> variableIds;

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public QuestionSearchDocument(Question question, ElasticsearchIndices index) {
    this.id = question.getId();
    this.number = question.getNumber();
    this.surveyId = question.getSurveyId();
    this.dataAcquisitionProjectId = question.getDataAcquisitionProjectId();
    this.technicalRepresentation = question.getTechnicalRepresentation();
    this.imageType = question.getImageType().name();
    this.successor = question.getSuccessor();
    this.predecessor = question.getPredecessor();
    this.variableIds = question.getVariableIds();
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
  
  public String getSurveyId() {
    return surveyId;
  }
  
  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
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

  public String getTechnicalRepresentation() {
    return technicalRepresentation;
  }

  public void setTechnicalRepresentation(String technicalRepresentation) {
    this.technicalRepresentation = technicalRepresentation;
  }

  public List<String> getPredecessor() {
    return predecessor;
  }

  public void setPredecessor(List<String> predecessor) {
    this.predecessor = predecessor;
  }

  public List<String> getSuccessor() {
    return successor;
  }

  public void setSuccessor(List<String> successor) {
    this.successor = successor;
  }

  public List<String> getVariableIds() {
    return variableIds;
  }

  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  } 
}
