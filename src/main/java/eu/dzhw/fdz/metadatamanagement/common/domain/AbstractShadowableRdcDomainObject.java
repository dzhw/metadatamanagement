package eu.dzhw.fdz.metadatamanagement.common.domain;

import lombok.Data;

import java.util.Objects;

/**
 * Base class for all rdc domain objects which can exist as multiple versions.
 */
@Data
public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject {

  private String masterId;
  private String successorId;
  private boolean shadow;

  public final void setMasterId(String masterId) {
    this.masterId = masterId;
    this.shadow = !Objects.equals(masterId, getId());
  }

  public final void setId(String id) {
    setIdInternal(id);
    this.shadow = !Objects.equals(masterId, getId());
  }

  /**
   * Set id on implementation of {@link AbstractShadowableRdcDomainObject}.
   * @param id Id
   */
  protected abstract void setIdInternal(String id);

}
