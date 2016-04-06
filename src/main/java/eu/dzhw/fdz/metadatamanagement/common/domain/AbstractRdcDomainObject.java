package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.google.common.base.MoreObjects;

/**
 * Base class for all fdz domain objects.
 * 
 * @author René Reitmann
 */
public abstract class AbstractRdcDomainObject {
  @Version
  private Long version;

  @CreatedDate
  private ZonedDateTime createdAt;

  @CreatedBy
  private String createdBy;

  @LastModifiedDate
  private ZonedDateTime lastModifiedAt;

  @LastModifiedBy
  private String lastModifiedBy;

  protected abstract String getId();

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public ZonedDateTime getLastModifiedAt() {
    return lastModifiedAt;
  }

  public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
    this.lastModifiedAt = lastModifiedAt;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getId());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AbstractRdcDomainObject domainObject = (AbstractRdcDomainObject) object;
    return Objects.equals(this.getId(), domainObject.getId());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", this.getId())
      .add("version", version)
      .add("createdAt", createdAt)
      .add("createdBy", createdBy)
      .add("lastModifiedAt", lastModifiedAt)
      .add("lastModifiedBy", lastModifiedBy)
      .toString();
  }
}
