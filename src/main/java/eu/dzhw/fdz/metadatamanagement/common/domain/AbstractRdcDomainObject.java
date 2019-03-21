package eu.dzhw.fdz.metadatamanagement.common.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

/**
 * Base class for all rdc domain objects. All domain objects inherit the fields from this base
 * class.
 */
@Data
public abstract class AbstractRdcDomainObject {
  /**
   * Number which is incremented on each save of this object.
   */
  @Version
  private Long version;

  /**
   * The date and time (in UTC) when this domain object was created.
   */
  @CreatedDate
  private LocalDateTime createdDate;

  /**
   * The name of the user which has created this object.
   */
  @CreatedBy
  private String createdBy;

  /**
   * The date and time when this object was last saved.
   */
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

  /**
   * The name of the user who last saved this object.
   */
  @LastModifiedBy
  private String lastModifiedBy;

  public abstract String getId();
}
