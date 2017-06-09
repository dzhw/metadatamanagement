package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import java.util.List;

import org.springframework.beans.BeanUtils;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections.RelatedPublicationSubDocumentProjection;

/**
 * Attributes of a publication which are stored in other search documents.
 *  
 * @author René Reitmann
 */
@SuppressWarnings("CPD-START")
public class RelatedPublicationSubDocument implements RelatedPublicationSubDocumentProjection {

  private String id;
  
  private String doi;
  
  private String title;
  
  private String authors;
  
  private String language;
  
  private List<String> studyIds;
  
  public RelatedPublicationSubDocument() {
    super();
  }
  
  public RelatedPublicationSubDocument(RelatedPublicationSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  @Override
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public List<String> getStudyIds() {
    return studyIds;
  }

  public void setStudyIds(List<String> studyIds) {
    this.studyIds = studyIds;
  }
}
