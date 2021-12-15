package eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;

import java.util.List;

/**
 * An extension of the {@link UserDto} which includes
 * the roles of the associated user.
 */
@JsonIgnoreProperties(
    value = "login",
    allowGetters = true
)
public class UserWithRolesDto extends UserDto {

  @Getter
  private final List<String> roles;

  /**
   * A constructor which will be used by Jackson JSON to create an instance of this class.
   *
   * @param id the id value of the user
   * @param name the name value (i.e. "login") of the user
   * @param mail the mail value (i.e. "email") of the user
   * @param langcode the langcode value (i.e. langKey) of the user
   * @param welcomeDialogDeactivated the field_welcome_dialog_deactivated flag (i.e.
   *                                   welcomeDialogDeactivated) of the user
   * @param roles the roles object array (i.e. an object array that includes the role name which
   *              will be used by this class) of the user
   */
  @JsonCreator
  public UserWithRolesDto(
      @JsonProperty("id") final String id,
      @JsonProperty("name") final String name,
      @JsonProperty("mail") final String mail,
      @JsonProperty("langcode") final String langcode,
      @JsonProperty("field_welcome_dialog_deactivated") final boolean welcomeDialogDeactivated,
      @JsonProperty("roles") final JsonNode roles
  ) {
    super(id, name, mail, langcode, welcomeDialogDeactivated);

    // Drupal's User API are not the same if the user has no roles. Only if the user has a role
    // will the User API response have a "roles" array field. Assume that if roles is not an
    // array, the user has no roles.
    if (roles.isArray()) {
      this.roles = roles.findValuesAsText("drupal_internal__id");
    } else {
      this.roles = List.of();
    }
  }
}
