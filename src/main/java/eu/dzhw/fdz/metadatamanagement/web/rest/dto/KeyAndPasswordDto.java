package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

/**
 * DTO for password reset.
 */
public class KeyAndPasswordDto {

  private String key;
  private String newPassword;

  public KeyAndPasswordDto() {}

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
