package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.projections.QuestionSubDocumentProjection;

/**
 * Attributes of a question which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class QuestionSubDocument implements QuestionSubDocumentProjection {
  
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String instrumentId;
  
  private Integer instrumentNumber;
  
  private String number;
  
  private I18nString questionText;
  
  private I18nString topic;
  
  public QuestionSubDocument() {
    super();
  }
  
  public QuestionSubDocument(QuestionSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
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
