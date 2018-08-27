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
public class QuestionSubDocument extends AbstractSubDocument
    implements QuestionSubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private String instrumentId;
  
  private Integer instrumentNumber;
  
  private String number;
  
  private I18nString questionText;
  
  private I18nString topic;
  
  private I18nString completeTitle;

  public QuestionSubDocument() {
    super();
  }
  
  /**
   * Create the subdocument.
   * 
   * @param projection the projection coming from mongo.
   * @param instrumentTitle the instruments title, can be null.
   */
  public QuestionSubDocument(QuestionSubDocumentProjection projection, I18nString instrumentTitle) {
    super();
    BeanUtils.copyProperties(projection, this);
    if (instrumentTitle != null) {
      this.completeTitle = I18nString.builder()
          .de("Frage " + projection.getNumber() + ": "
              + (instrumentTitle.getDe() != null ? instrumentTitle.getDe()
                  : instrumentTitle.getEn())
              + " (" + projection.getId() + ")")
          .en("Question " + projection.getNumber() + ": "
              + (instrumentTitle.getEn() != null ? instrumentTitle.getEn()
                  : instrumentTitle.getDe())
              + " (" + projection.getId() + ")")
          .build();
    } else {
      this.completeTitle =
          I18nString.builder().de("Frage " + projection.getNumber() + ": " + projection.getId())
              .en("Question " + projection.getNumber() + ": " + projection.getId()).build();
    }
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }

  @Override
  public String getInstrumentId() {
    return instrumentId;
  }

  public void setInstrumentId(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  @Override
  public Integer getInstrumentNumber() {
    return instrumentNumber;
  }

  public void setInstrumentNumber(Integer instrumentNumber) {
    this.instrumentNumber = instrumentNumber;
  }

  @Override
  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public I18nString getQuestionText() {
    return questionText;
  }

  public void setQuestionText(I18nString questionText) {
    this.questionText = questionText;
  }

  @Override
  public I18nString getTopic() {
    return topic;
  }

  public void setTopic(I18nString topic) {
    this.topic = topic;
  }

  @Override
  public I18nString getCompleteTitle() {
    return completeTitle;
  }
}
