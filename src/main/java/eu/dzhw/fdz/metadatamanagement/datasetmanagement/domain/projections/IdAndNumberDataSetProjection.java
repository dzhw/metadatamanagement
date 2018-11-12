package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Get only the dataset id and the number of a data set.
 *
 * @author René Reitmann
 */
public interface IdAndNumberDataSetProjection 
    extends IdAndVersionProjection {
  Integer getNumber();
}
