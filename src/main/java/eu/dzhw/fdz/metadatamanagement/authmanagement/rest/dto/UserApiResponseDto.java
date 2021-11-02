package eu.dzhw.fdz.metadatamanagement.authmanagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO which acts as a wrapper for the "data" field in the User API response.
 */
@Getter
@Setter
public class UserApiResponseDto {

  private List<UserDto> data;

  /**
   * A DTO which contains all desired fields related to a User in the User API response.
   */
  public static class UserDto {

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
}
