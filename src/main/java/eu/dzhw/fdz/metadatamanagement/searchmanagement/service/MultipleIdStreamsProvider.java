package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import java.util.List;
import java.util.stream.Stream;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Definition of closures which can be executed asyncronously 
 * to provide a list of streams of document ids which need to be updated
 * in elasticsearch.
 * 
 * @author René Reitmann
 */
@FunctionalInterface
public interface MultipleIdStreamsProvider {
  List<Stream<IdAndVersionProjection>> get();
}
