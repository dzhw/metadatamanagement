package eu.dzhw.fdz.metadatamanagement.common.domain.projections;

import java.time.ZonedDateTime;

/**
 * Projection used to expose all attributes (including ids and versions). Spring Data rest does not
 * expose ids and version per default in the json.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public interface AbstractRdcDomainObjectProjection {

  String getId();

  Long getVersion();

  String getCreatedBy();

  ZonedDateTime getCreatedAt();

  ZonedDateTime getLastModifiedAt();

  String getLastModifiedBy();
}
