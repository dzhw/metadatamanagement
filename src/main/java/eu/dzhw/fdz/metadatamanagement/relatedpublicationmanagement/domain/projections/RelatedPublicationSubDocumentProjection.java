package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections;

/**
 * Subset of related publication attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface RelatedPublicationSubDocumentProjection {
  String getId();

  String getDoi();

  String getTitle();

  String getAuthors();
}
