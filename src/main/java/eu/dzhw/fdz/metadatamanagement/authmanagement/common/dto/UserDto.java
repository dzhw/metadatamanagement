package eu.dzhw.fdz.metadatamanagement.authmanagement.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A DTO which contains all desired fields related to a User in the User API response.
 */
@Getter
public class UserDto {
  private final String login;
  private final String email;
  private final String langKey;
  private final boolean welcomeDialogDeactivated;

  /**
   * A constructor which will be used by Jackson JSON to create an instance of this class.
   *
   * @param name the name value (i.e. "login") of the user
   * @param mail the mail value (i.e. "email") of the user
   * @param langcode the langcode value (i.e. langKey) of the user
   * @param welcome_dialog_deactivated the welcome_dialog_deactivated flag (i.e.
   *                                   welcomeDialogDeactivated) of the user
   */
  @JsonCreator
  public UserDto(
      @JsonProperty(value = "name") final String name,
      @JsonProperty("mail") final String mail,
      @JsonProperty("langcode") final String langcode,
      @JsonProperty("welcome_dialog_deactivated") final boolean welcome_dialog_deactivated
  ) {
    this.login = name;
    this.email = mail;
    this.langKey = langcode;
    this.welcomeDialogDeactivated = welcome_dialog_deactivated;
  }
}
