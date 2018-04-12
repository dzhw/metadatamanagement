package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Definition of closures which can be executed asyncronously 
 * to provide a document id which need to be updated in elasticsearch.
 * 
 * @author René Reitmann
 */
@FunctionalInterface
public interface IdProvider {

  IdAndVersionProjection get();
}
