package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

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
  
  private String type;
  //TODO rreitmann add attachment metadata
  
  private String surveyId;
  
  private String dataAcquisitionProjectId;
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public InstrumentSearchDocument(Instrument instrument, ElasticsearchIndices index) {
    this.id = instrument.getId();
    this.dataAcquisitionProjectId = instrument.getDataAcquisitionProjectId();
    this.type = instrument.getType();
    this.surveyId = instrument.getSurveyId();
    createI18nAttributes(instrument, index);
  }

  private void createI18nAttributes(Instrument instrument, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        title = instrument.getTitle() != null ? instrument.getTitle().getDe() : null;
        break;
      case METADATA_EN:
        title = instrument.getTitle() != null ? instrument.getTitle().getEn() : null;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSurveyId() {
    return surveyId;
  }

  public void setSurveyId(String surveyId) {
    this.surveyId = surveyId;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
}
