package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * A user.
 */
@Document(collection = "jhi_user")
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.usermanagement.domain.builders")
public class User extends AbstractRdcDomainObject implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @NotNull(message = "user-management.error.user.login.notNull")
  @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
  @Size(min = 1, max = 50)
  private String login;

  @JsonIgnore
  @NotNull(message = "user-management.error.user.password.notNull")
  @Size(min = 60, max = 60)
  private String password;

  @Size(max = 50)
  @Field("first_name")
  private String firstName;

  @Size(max = 50)
  @Field("last_name")
  private String lastName;

  @Email
  @Size(max = 100)
  private String email;

  private boolean activated = false;

  @Size(min = 2, max = 5)
  @Field("lang_key")
  private String langKey;

  @Size(max = 20)
  @Field("activation_key")
  @JsonIgnore
  private String activationKey;

  @Size(max = 20)
  @Field("reset_key")
  private String resetKey;

  @Field("reset_date")
  private LocalDateTime resetDate = null;

  @JsonIgnore
  private Set<Authority> authorities = new HashSet<>();

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean getActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  public String getActivationKey() {
    return activationKey;
  }

  public void setActivationKey(String activationKey) {
    this.activationKey = activationKey;
  }

  public String getResetKey() {
    return resetKey;
  }

  public void setResetKey(String resetKey) {
    this.resetKey = resetKey;
  }

  public LocalDateTime getResetDate() {
    return resetDate;
  }

  public void setResetDate(LocalDateTime resetDate) {
    this.resetDate = resetDate;
  }

  public String getLangKey() {
    return langKey;
  }

  public void setLangKey(String langKey) {
    this.langKey = langKey;
  }

  public Set<Authority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<Authority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    User user = (User) object;

    if (login != null && !login.equals(user.login)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {

    if (login == null) {
      return 0;
    }

    return login.hashCode();
  }

  @Override
  public String toString() {
    return "User{" + "login='" + login + '\'' + ", firstName='" + firstName + '\'' + ", lastName='"
        + lastName + '\'' + ", email='" + email + '\'' + ", activated='" + activated + '\''
        + ", langKey='" + langKey + '\'' + ", activationKey='" + activationKey + '\'' + "}";
  }
}
