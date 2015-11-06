package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import eu.dzhw.fdz.metadatamanagement.domain.Authority;
import eu.dzhw.fdz.metadatamanagement.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
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

  public UserDto() {}

  /**
   * A constructor with a user representation.
   * @param user a representation of a user.
   */
  public UserDto(User user) {
    this(user.getLogin(), null, user.getFirstName(), user.getLastName(), user.getEmail(),
        user.getActivated(), user.getLangKey(),
        user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet()));
  }

  /**
   * Constructor with information about an user.
   * @param login login of an user
   * @param password password of an user
   * @param firstName the first name of an user
   * @param lastName the last name of an user
   * @param email the email address of an user
   * @param activated the activation status of an user
   * @param langKey the language key of an user
   * @param authorities the authorities of a user
   */
  public UserDto(String login, String password, String firstName, String lastName, String email,
      boolean activated, String langKey, Set<String> authorities) {

    this.login = login;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.activated = activated;
    this.langKey = langKey;
    this.authorities = authorities;
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

  @Override
  public String toString() {
    return "UserDTO{" + "login='" + login + '\'' + ", password='" + password + '\''
        + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", email='"
        + email + '\'' + ", activated=" + activated + ", langKey='" + langKey + '\''
        + ", authorities=" + authorities + "}";
  }
}
