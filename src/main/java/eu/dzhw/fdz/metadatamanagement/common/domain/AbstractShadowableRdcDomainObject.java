package eu.dzhw.fdz.metadatamanagement.common.domain;

import lombok.Data;

/**
 * Base class for all rdc domain objects which can exist as multiple versions.
 */
@Data
public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject {

  private String masterId;
  private String successorId;
  private boolean shadow;
}
