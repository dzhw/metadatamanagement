package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.projections;

/**
 * Projection for returning a variable with id and version and dataSet id.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionAndDataSetProjection extends IdAndVersionVariableProjection {
  String getDataSetId();
}
