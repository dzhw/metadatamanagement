package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import io.searchbox.annotations.JestId;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
//TODO just the id. update and add all other attributes.
public class RelatedPublicationSearchDocument {
  @JestId
  private String id;
  
  /**
   * Create the search document from the domain object depending on the language (index).
   */
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication) {
    this.id = relatedPublication.getId();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
