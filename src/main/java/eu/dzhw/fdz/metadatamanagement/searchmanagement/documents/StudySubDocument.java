package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.Person;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.projection.StudySubDocumentProjection;

/**
 * Attributes of a study which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class StudySubDocument extends AbstractRdcDomainObject
    implements StudySubDocumentProjection {
  private String id;
  
  private String dataAcquisitionProjectId;
  
  private I18nString institution;
  
  private I18nString sponsor;
  
  private I18nString studySeries;
  
  private I18nString title;
  
  private List<Person> authors;
  
  private String doi;
  
  private I18nString surveyDesign;
  
  public StudySubDocument() {
    super();
  }
  
  /**
   * Create a StudySubdocument from a projection and a doi.
   * @param projection a study projection
   * @param doi a doi or null
   */
  public StudySubDocument(StudySubDocumentProjection projection, String doi) {
    super();
    BeanUtils.copyProperties(projection, this);
    this.doi = doi;
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
  public I18nString getStudySeries() {
    return studySeries;
  }

  public void setStudySeries(I18nString studySeries) {
    this.studySeries = studySeries;
  }

  @Override
  public I18nString getTitle() {
    return title;
  }

  public void setTitle(I18nString title) {
    this.title = title;
  }

  public String getDoi() {
    return doi;
  }
  
  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Override
  public I18nString getSurveyDesign() {
    return surveyDesign;
  }

  public void setSurveyDesign(I18nString surveyDesign) {
    this.surveyDesign = surveyDesign;
  }
}
