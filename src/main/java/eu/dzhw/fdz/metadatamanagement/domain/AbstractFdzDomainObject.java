package eu.dzhw.fdz.metadatamanagement.domain;

import java.time.ZonedDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.domain.projections.AbstractFdzDomainObjectProjection;

/**
 * Base class for all fdz domain onjects.
 * 
 * @author René Reitmann
 */
public abstract class AbstractFdzDomainObject implements AbstractFdzDomainObjectProjection {
  @Id
  private String id;
  
  @Version
  private long version;
  
  @CreatedDate
  private ZonedDateTime createdDate;
  
  @CreatedBy
  private String createdBy;
  
  @LastModifiedDate
  private ZonedDateTime lastModifiedDate;
  
  @LastModifiedBy
  private String lastModifiedBy;

  /* (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractFdzDomainObjectInterface#getId()
   */
  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /* (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractFdzDomainObjectInterface#getVersion()
   */
  @Override
  public long getVersion() {
    return version;
  }

  public void setVersion(long version) {
    this.version = version;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  /* (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractFdzDomainObjectInterface#getCreatedBy()
   */
  @Override
  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  /* (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.domain.
   * AbstractFdzDomainObjectInterface#getLastModifiedDate()
   */
  @Override
  public ZonedDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /* (non-Javadoc)
   * @see eu.dzhw.fdz.metadatamanagement.domain.AbstractFdzDomainObjectInterface#getLastModifiedBy()
   */
  @Override
  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AbstractFdzDomainObjectProjection domainObject = (AbstractFdzDomainObjectProjection) object;
    return Objects.equals(id, domainObject.getId());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("id", id)
      .add("version", version)
      .add("createdDate", createdDate)
      .add("createdBy", createdBy)
      .add("lastModifiedDate", lastModifiedDate)
      .add("lastModifiedBy", lastModifiedBy)
      .toString();
  } 
}
