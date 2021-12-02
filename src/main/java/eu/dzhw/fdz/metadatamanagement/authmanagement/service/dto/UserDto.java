package eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * A DTO which contains all desired fields related to a User in the User API response.
 */
@Getter
public class UserDto {
  private final String id;
  private final String login;
  private final String email;
  private final String langKey;
  private final boolean welcomeDialogDeactivated;

  /**
   * A constructor which will be used by Jackson JSON to create an instance of this class.
   *
   * @param id the id value of the user
   * @param name the name value (i.e. "login") of the user
   * @param mail the mail value (i.e. "email") of the user
   * @param langcode the langcode value (i.e. langKey) of the user
   * @param welcomeDialogDeactivated the field_welcome_dialog_deactivated flag (i.e.
   *                                   welcomeDialogDeactivated) of the user
   */
  @JsonCreator
  public UserDto(
      @JsonProperty(value = "id") final String id,
      @JsonProperty(value = "name") final String name,
      @JsonProperty("mail") final String mail,
      @JsonProperty("langcode") final String langcode,
      @JsonProperty("field_welcome_dialog_deactivated") final boolean welcomeDialogDeactivated
  ) {
    this.id = id;
    this.login = name;
    this.email = mail;
    this.langKey = langcode;
    this.welcomeDialogDeactivated = welcomeDialogDeactivated;
  }
}
