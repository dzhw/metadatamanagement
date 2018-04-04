package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.caching.CacheAspect;

/**
 * List of {@link DataSetRepository} methods which are cached per request (see {@link CacheAspect}.
 * 
 * @author René Reitmann
 */
public interface CacheableRepositoryMethods {
  List<IdAndVersionProjection> findIdsByDataAcquisitionProjectIdAndNumber(
      String dataAcquisitionProjectId, Integer number);
}
