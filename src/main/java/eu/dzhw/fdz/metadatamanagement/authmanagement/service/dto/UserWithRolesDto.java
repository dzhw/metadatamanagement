package eu.dzhw.fdz.metadatamanagement.authmanagement.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * An extension of the {@link UserDto} which includes
 * the roles of the associated user.
 */
// This is a read only object
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
      @JsonProperty("roles") final List<RoleDto> roles
  ) {
    super(id, name, mail, langcode, welcomeDialogDeactivated);

    if (roles != null) {
      this.roles = roles.stream().map(r -> r.name).collect(Collectors.toList());
    } else {
      this.roles = List.of();
    }
  }

  /**
   * A wrapper class which exists as an in-between for Jackson JSON to change the JSON's role
   * array object field to a list of role names.
   */
  public static class RoleDto {
    private final String name;

    public RoleDto(@JsonProperty("drupal_internal__id") final String drupalInternalId) {
      this.name = drupalInternalId;
    }
  }
}
