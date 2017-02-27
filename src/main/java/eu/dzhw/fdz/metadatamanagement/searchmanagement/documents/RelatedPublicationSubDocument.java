package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

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
  
  public RelatedPublicationSubDocument() {
    super();
  }
  
  public RelatedPublicationSubDocument(RelatedPublicationSubDocumentProjection projection) {
    super();
    BeanUtils.copyProperties(projection, this);
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDoi() {
    return doi;
  }

  public void setDoi(String doi) {
    this.doi = doi;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }
}
