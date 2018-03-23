package eu.dzhw.fdz.metadatamanagement.variablemanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.caching.CacheAspect;

/**
 * List of {@link VariableRepository} methods which are cached per 
 * request (see {@link CacheAspect}.
 * 
 * @author René Reitmann
 */
public interface CacheableRepositoryMethods {
  List<IdAndVersionProjection> findIdsByNameAndDataSetId(String name, String dataSetId);
}
