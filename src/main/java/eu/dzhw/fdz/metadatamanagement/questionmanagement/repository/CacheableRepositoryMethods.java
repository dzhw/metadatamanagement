package eu.dzhw.fdz.metadatamanagement.questionmanagement.repository;

import java.util.List;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.common.repository.caching.CacheAspect;

/**
 * List of {@link QuestionRepository} methods which are cached per 
 * request (see {@link CacheAspect}.
 * 
 * @author René Reitmann
 */
public interface CacheableRepositoryMethods {
  List<IdAndVersionProjection> findIdsByInstrumentIdAndNumber(String instrumentId,
      String number);
}
