package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.projections;

import org.springframework.data.rest.core.config.Projection;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * {@link IdAndVersionProjection} for {@link RelatedPublication}s.
 * 
 * @author René Reitmann
 */
@Projection(name = "id-and-version", types = RelatedPublication.class)
public interface IdAndVersionRelatedPublicationProjection extends IdAndVersionProjection {
  
}

