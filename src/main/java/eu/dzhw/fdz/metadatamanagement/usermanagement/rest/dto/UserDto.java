package eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.User;
import lombok.ToString;

/**
 * A DTO representing a user, with his authorities.
 */
@ToString
public class UserDto {

  public static final int PASSWORD_MIN_LENGTH = 5;
  public static final int PASSWORD_MAX_LENGTH = 100;

  @Pattern(regexp = "^[a-z0-9]*$")
  @NotNull
  @Size(min = 1, max = 50)
  private String login;

  @NotNull
  @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
  private String password;

  @Size(max = 50)
  private String firstName;

  @Size(max = 50)
  private String lastName;

  @Email
  @Size(min = 5, max = 100)
  private String email;

  private boolean activated = false;

  @Size(min = 2, max = 5)
  private String langKey;

  private Set<String> authorities;

  private boolean welcomeDialogDeactivated;

  public UserDto() {}

  /**
   * Create the dto.
   */
  public UserDto(User user) {
    this(user.getLogin(), null, user.getFirstName(), user.getLastName(), user.getEmail(),
        user.isActivated(), user.getLangKey(), user.getAuthorities()
          .stream()
          .map(Authority::getName)
          .collect(Collectors.toSet()), user.isWelcomeDialogDeactivated());
  }

  /**
   * Create the dto.
   */
  public UserDto(String login, String password, String firstName, String lastName, String email,
      boolean activated, String langKey, Set<String> authorities,
      boolean welcomeDialogDeactivated) {

    this.login = login;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.activated = activated;
    this.langKey = langKey;
    this.authorities = authorities;
    this.welcomeDialogDeactivated = welcomeDialogDeactivated;
  }

  public String getPassword() {
    return password;
  }

  public String getLogin() {
    return login;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public boolean isActivated() {
    return activated;
  }

  public String getLangKey() {
    return langKey;
  }

  public Set<String> getAuthorities() {
    return authorities;
  }

  public boolean isWelcomeDialogDeactivated() {
    return welcomeDialogDeactivated;
  }
}
