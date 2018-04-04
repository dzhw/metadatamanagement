package eu.dzhw.fdz.metadatamanagement.surveymanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.caching.CacheAspect;

/**
 * List of {@link SurveyRepository} methods which are cached per 
 * request (see {@link CacheAspect}.
 * 
 * @author René Reitmann
 */
public interface CacheableRepositoryMethods {
  List<IdAndVersionProjection> findIdsByNumberAndDataAcquisitionProjectId(Integer number,
      String dataAcquisitionProjectId);
}
