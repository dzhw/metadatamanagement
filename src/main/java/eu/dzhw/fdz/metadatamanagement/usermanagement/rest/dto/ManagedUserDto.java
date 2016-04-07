package eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto;

import java.time.LocalDateTime;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserDto extends UserDto {

  private String id;

  private LocalDateTime createdDate;

  private String lastModifiedBy;

  private LocalDateTime lastModifiedDate;

  public ManagedUserDto() {}

  /**
   * Create the dto from a given user.
   */
  public ManagedUserDto(User user) {
    super(user);
    this.id = user.getId();
    this.createdDate = user.getCreatedDate();
    this.lastModifiedBy = user.getLastModifiedBy();
    this.lastModifiedDate = user.getLastModifiedDate();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastModifiedBy() {
    return lastModifiedBy;
  }

  public void setLastModifiedBy(String lastModifiedBy) {
    this.lastModifiedBy = lastModifiedBy;
  }

  public LocalDateTime getLastModifiedDate() {
    return lastModifiedDate;
  }

  public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  @Override
  public String toString() {
    return "ManagedUserDTO{" + "id=" + id + ", createdDate=" + createdDate + ", lastModifiedBy='"
        + lastModifiedBy + '\'' + ", lastModifiedDate=" + lastModifiedDate + "} "
        + super.toString();
  }
}
