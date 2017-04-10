package eu.dzhw.fdz.metadatamanagement.common.domain.projections;

/**
 * Minimal projection which can be used when we only need the id of a
 * domain object.
 * 
 * @author René Reitmann
 */
public interface IdAndVersionProjection {
  String getId();
  
  Long getVersion();
}
