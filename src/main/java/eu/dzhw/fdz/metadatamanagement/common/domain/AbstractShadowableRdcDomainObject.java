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

  /**
   * The id shared between all shadow copies of a domain object. It points to
   * the most recent version of the domain object.
   */
  private String masterId;

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
    this.masterId = masterId;
    this.shadow = !Objects.equals(masterId, getId());
  }

  /**
   * Set the id for the document. This will modify it's
   * {@link AbstractShadowableRdcDomainObject#shadow} field as well.
   * @param id Document id
   */
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
