package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;

/**
 * Projection for returning a variable with id and version and dataSet id.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndDataSetProjection extends IdAndVersionProjection {
  String getDataSetId();
}
