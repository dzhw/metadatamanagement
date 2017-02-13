package eu.dzhw.fdz.metadatamanagement.searchmanagement.documents;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Representation of a related publication which is stored in elasticsearch.
 *
 * @author Daniel Katzberg
 */
public class RelatedPublicationSearchDocument extends RelatedPublication {
  
  public RelatedPublicationSearchDocument(RelatedPublication relatedPublication) {
    super(relatedPublication);
  }
}
