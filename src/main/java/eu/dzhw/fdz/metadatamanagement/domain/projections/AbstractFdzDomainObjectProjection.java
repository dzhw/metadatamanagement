package eu.dzhw.fdz.metadatamanagement.domain.projections;

import java.time.ZonedDateTime;

/**
 * Interface used in projections to expose all attributes.
 * Id and version are handled differently per default in spring data rest!
 * 
 * @author René Reitmann
 */
public interface AbstractFdzDomainObjectProjection {

  String getId();

  long getVersion();

  String getCreatedBy();

  ZonedDateTime getLastModifiedDate();

  String getLastModifiedBy();
}
