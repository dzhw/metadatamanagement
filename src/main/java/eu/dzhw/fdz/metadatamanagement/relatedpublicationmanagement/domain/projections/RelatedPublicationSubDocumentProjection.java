package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.AbstractRdcDomainObjectProjection;

/**
 * Subset of related publication attributes which can be used in other search documents
 * as sub document and as projection for mongo.
 * 
 * @author René Reitmann
 */
public interface RelatedPublicationSubDocumentProjection 
    extends AbstractRdcDomainObjectProjection {
  String getDoi();

  String getTitle();

  String getAuthors();
}
