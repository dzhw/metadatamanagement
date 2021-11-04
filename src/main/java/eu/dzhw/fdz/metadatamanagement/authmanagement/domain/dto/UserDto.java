package eu.dzhw.fdz.metadatamanagement.authmanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO which contains all desired fields related to a User in the User API response.
 */
public class UserDto {

  @Getter
  @Setter
  @JsonProperty(value = "name")
  private String login;

  @Getter
  @Setter
  @JsonProperty(value = "mail")
  private String email;

  @Getter
  @Setter
  @JsonProperty(value = "langcode")
  private String langKey;

  @Getter
  @Setter
  @JsonProperty(value = "welcome_dialog_deactivated")
  private boolean welcomeDialogDeactivated;
}
