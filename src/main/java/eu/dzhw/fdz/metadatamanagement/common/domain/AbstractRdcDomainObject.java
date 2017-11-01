package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import lombok.Data;

/**
 * Base class for all fdz domain objects.
 * 
 * @author René Reitmann
 */
@Data
public abstract class AbstractRdcDomainObject {
  @Version
  private Long version;

  @CreatedDate
  private LocalDateTime createdDate;

  @CreatedBy
  private String createdBy;

  @LastModifiedDate
  private LocalDateTime lastModifiedDate;

  @LastModifiedBy
  private String lastModifiedBy;

  public abstract String getId();
}
