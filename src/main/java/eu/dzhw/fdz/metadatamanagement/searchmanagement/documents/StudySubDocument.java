package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;

/**
 * Attributes of a study which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class StudySubDocument implements StudySubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString description;
  
  private I18nString institution;
  
  private I18nString sponsor;
  
  private I18nString surveySeries;
  
  private I18nString title;
  
  private List<Person> authors;
  
  private String doi;
  
  private I18nString annotations;
  
  public StudySubDocument() {
    super();
  }
  
  public StudySubDocument(StudySubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }

  @Override
  public List<Person> getAuthors() {
    return authors;
  }

  public void setAuthors(List<Person> authors) {
    this.authors = authors;
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
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  @Override
  public I18nString getInstitution() {
    return institution;
  }

  public void setInstitution(I18nString institution) {
    this.institution = institution;
  }

  @Override
  public I18nString getSponsor() {
    return sponsor;
  }

  public void setSponsor(I18nString sponsor) {
    this.sponsor = sponsor;
  }

  @Override
  public I18nString getSurveySeries() {
    return surveySeries;
  }

  public void setSurveySeries(I18nString surveySeries) {
    this.surveySeries = surveySeries;
  }

  @Override
  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  @Override
  public String getDoi() {
    return doi;
  }
  
  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Override
  public I18nString getAnnotations() {
    return annotations;
  }

  public void setAnnotations(I18nString annotations) {
    this.annotations = annotations;
  }
}
