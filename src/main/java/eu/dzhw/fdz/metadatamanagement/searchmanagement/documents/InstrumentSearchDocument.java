package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import io.searchbox.annotations.JestId;

/**
 * Representation of an instrument which is stored in elasticsearch.
 *
 * @author René Reitmann
 */
public class InstrumentSearchDocument {
  @JestId
  private String id;
  
  private String title;
  
  private String description;
  
  private String type;
  
  private String dataAcquisitionProjectId;

  private List<String> surveyIds;
  
  private List<Integer> surveyNumbers;
  
  private Integer number;
 
  
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public InstrumentSearchDocument(Instrument instrument, ElasticsearchIndices index) {
    this.id = instrument.getId();
    this.dataAcquisitionProjectId = instrument.getDataAcquisitionProjectId();
    this.type = instrument.getType();
    this.surveyIds = instrument.getSurveyIds();
    this.surveyNumbers = instrument.getSurveyNumbers();
    this.number = instrument.getNumber();
    createI18nAttributes(instrument, index);
  }

  private void createI18nAttributes(Instrument instrument, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        title = instrument.getTitle() != null ? instrument.getTitle().getDe() : null;
        description = instrument.getDescription() != null 
            ? instrument.getDescription().getDe() : null;
        break;
      case METADATA_EN:
        title = instrument.getTitle() != null ? instrument.getTitle().getEn() : null;
        description = instrument.getDescription() != null 
            ? instrument.getDescription().getEn() : null;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public List<String> getSurveyIds() {
    return surveyIds;
  }
  
  public void setSurveyIds(List<String> surveyIds) {
    this.surveyIds = surveyIds;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }


  public List<Integer> getSurveyNumbers() {
    return surveyNumbers;
  }

  public void setSurveyNumbers(List<Integer> surveyNumbers) {
    this.surveyNumbers = surveyNumbers;
  }

  public Integer getNumber() {
    return number;
  }

  public void setNumber(Integer number) {
    this.number = number;
  }
}
