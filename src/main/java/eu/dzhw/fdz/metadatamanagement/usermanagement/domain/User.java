package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A user.
 */
@Document(collection = "jhi_user")
@Data
@EqualsAndHashCode(callSuper = false, of = "login")
@ToString(callSuper = true)
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class User extends AbstractRdcDomainObject implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @NotNull(message = "user-management.error.user.login.not-null")
  @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
  @Size(min = 1, max = 50)
  @Indexed(unique = true)
  private String login;

  @JsonIgnore
  @NotNull(message = "user-management.error.user.password.not-null")
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
  @Indexed(unique = true)
  private String email;

  @Builder.Default
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
  @Builder.Default
  private LocalDateTime resetDate = null;

  @JsonIgnore
  private Set<Authority> authorities;
}
