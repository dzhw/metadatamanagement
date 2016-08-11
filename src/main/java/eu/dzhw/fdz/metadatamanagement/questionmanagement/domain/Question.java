package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Question.
 */
@Document(collection = "questions")
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.builders")
public class Question extends AbstractRdcDomainObject {
  
  /* Domain Object Attributes */
  @Id
  private String id;
  private String number;
  private I18nString questionText;
  private I18nString instruction;
  private I18nString introduction;
  private I18nString type;
  private I18nString additionalQuestionText;
  private ImageType imageType;
  private String technicalRepresentation;
  private List<String> predecessor;
  private List<String> successor;
  private List<String> variableIds;
    
  /* Foreign Keys */
  @Indexed
  private String dataAcquisitionProjectId;
  
  public String getId() {
    return id;
  }
  
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

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }
  
  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
  
  public List<String> getVariableIds() {
    return variableIds;
  }
  
  public void setVariableIds(List<String> variableIds) {
    this.variableIds = variableIds;
  }

  @Override
  public String toString() {
    return "Frage [id=" + id + ", number=" + number + ", questionText=" + questionText
        + ", instruction=" + instruction + ", introduction=" + introduction + ", type=" + type
        + ", additionalQuestionText=" + additionalQuestionText + ", imageType=" + imageType 
        + ", technicalRepresentation="
        + technicalRepresentation + ", predecessor=" + predecessor
        + ", successor=" + successor + ", dataAcquisitionProjectId=" + dataAcquisitionProjectId
        + ", variableIds=" + variableIds + "]";
  }
  

}
