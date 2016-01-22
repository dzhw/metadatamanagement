package eu.dzhw.fdz.metadatamanagement.domain;

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
public abstract class AbstractFdzDomainObject {
  @Version
  private Long version;
  
  @CreatedDate
  private ZonedDateTime createdDate;
  
  @CreatedBy
  private String createdBy;
  
  @LastModifiedDate
  private ZonedDateTime lastModifiedDate;
  
  @LastModifiedBy
  private String lastModifiedBy;
  
  protected abstract String getId();

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public ZonedDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
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
    AbstractFdzDomainObject domainObject = (AbstractFdzDomainObject) object;
    return Objects.equals(this.getId(), domainObject.getId());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", this.getId())
      .add("version", version)
      .add("createdDate", createdDate)
      .add("createdBy", createdBy)
      .add("lastModifiedDate", lastModifiedDate)
      .add("lastModifiedBy", lastModifiedBy)
      .toString();
  } 
}
