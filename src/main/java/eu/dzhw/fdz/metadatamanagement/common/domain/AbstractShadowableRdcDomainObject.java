package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.util.Objects;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * Base class for all rdc domain objects which can exist as multiple versions.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractShadowableRdcDomainObject extends AbstractRdcDomainObject {

  private static final long serialVersionUID = 8006229461938445620L;

  /**
   * The document id which is the successor to this shadow copy.
   */
  private String successorId;

  /**
   * Determines whether this document is a shadow copy.
   */
  @Setter(AccessLevel.NONE)
  @Indexed
  private boolean shadow;

  /**
   * Set the master id for the document. This will modify it's
   * {@link AbstractShadowableRdcDomainObject#shadow} field as well.
   * @param masterId Master id
   */
  public final void setMasterId(String masterId) {
    setMasterIdInternal(masterId);
    this.shadow = !Objects.equals(getMasterId(), getId());
  }

  /**
   * Set the id for the document. This will modify it's
   * {@link AbstractShadowableRdcDomainObject#shadow} field as well.
   * @param id Document id
   */
  public final void setId(String id) {
    setIdInternal(id);
    this.shadow = !Objects.equals(getMasterId(), getId());
  }

  /**
   * Set masterId on implementations of {@link AbstractShadowableRdcDomainObject}.
   * @param masterId Master Id
   */
  protected abstract void setMasterIdInternal(String masterId);

  /**
   * Set id on implementation of {@link AbstractShadowableRdcDomainObject}.
   * @param id Id
   */
  protected abstract void setIdInternal(String id);

  public abstract String getMasterId();

}
