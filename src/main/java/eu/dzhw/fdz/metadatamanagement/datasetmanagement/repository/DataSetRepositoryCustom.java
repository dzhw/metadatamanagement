package eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository;

import java.util.List;

/**
 * Interface for custom repository methods.
 * 
 * @author René Reitmann
 */
public interface DataSetRepositoryCustom {
  List<String> findAllAccessWays(String studyId);
}
