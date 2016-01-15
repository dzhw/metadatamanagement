package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.time.ZonedDateTime;

/**
 * Projection used to expose all attributes (including ids and versions).
 * Spring Data rest does not expose ids and version per default in the json.
 * 
 * @author René Reitmann
 */
public interface AbstractFdzDomainObjectProjection {

  String getId();

  long getVersion();

  String getCreatedBy();
  
  ZonedDateTime getCreatedDate();

  ZonedDateTime getLastModifiedDate();

  String getLastModifiedBy();
}
