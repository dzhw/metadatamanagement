package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.service.ElasticsearchIndices;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import io.searchbox.annotations.JestId;

/**
 * Representation of a study which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
public class StudySearchDocument {
  @JestId
  private String id;
  
  private String title;
  
  private String description;
  
  private String institution;
  
  private String surveySeries;
  
  private String sponsor;
  
  private String citationHint;
  
  private String authors;  

  private String dataAcquisitionProjectId;

  

  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public StudySearchDocument(Study study, ElasticsearchIndices index) {
    this.id = study.getId();
    this.dataAcquisitionProjectId = study.getDataAcquisitionProjectId();
    this.authors = study.getAuthors();
    createI18nAttributes(study, index);
  }

  private void createI18nAttributes(Study study, ElasticsearchIndices index) {
    switch (index) {
      case METADATA_DE:
        this.title = study.getTitle() != null ? study.getTitle().getDe() : null;
        this.description = study.getDescription() != null ? study.getDescription().getDe() : null;
        this.institution = study.getInstitution() != null ? study.getInstitution().getDe() : null;
        this.surveySeries = study.getSurveySeries() != null 
            ? study.getSurveySeries().getDe() : null;
        this.sponsor = study.getSponsor() != null ? study.getSponsor().getDe() : null;
        this.citationHint = study.getCitationHint() != null 
            ? study.getCitationHint().getDe() : null;
        break;
      case METADATA_EN:
        this.title = study.getTitle() != null ? study.getTitle().getEn() : null;
        this.description = study.getDescription() != null ? study.getDescription().getEn() : null;
        this.institution = study.getInstitution() != null ? study.getInstitution().getEn() : null;
        this.surveySeries = study.getSurveySeries() != null 
            ? study.getSurveySeries().getEn() : null;
        this.sponsor = study.getSponsor() != null ? study.getSponsor().getEn() : null;
        this.citationHint = study.getCitationHint() != null 
            ? study.getCitationHint().getEn() : null;
        break;
      default:
        throw new RuntimeException("Unknown index:" + index);
    }
  }

  /* GETTER / SETTER */
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getInstitution() {
    return institution;
  }

  public void setInstitution(String institution) {
    this.institution = institution;
  }

  public String getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(String surveySeries) {
    this.surveySeries = surveySeries;
  }

  public String getSponsor() {
    return sponsor;
  }

  public void setSponsor(String sponsor) {
    this.sponsor = sponsor;
  }

  public String getCitationHint() {
    return citationHint;
  }

  public void setCitationHint(String citationHint) {
    this.citationHint = citationHint;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getDataAcquisitionProjectId() {
    return dataAcquisitionProjectId;
  }

  public void setDataAcquisitionProjectId(String dataAcquisitionProjectId) {
    this.dataAcquisitionProjectId = dataAcquisitionProjectId;
  }
}
