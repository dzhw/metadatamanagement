package eu.dzhw.fdz.metadatamanagement.authmanagement.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * An immutable POJO representation of the User data stored in the Authentication Server.
 */
@EqualsAndHashCode(callSuper = false, of = "login")
@ToString(callSuper = true)
@Data
@Builder
@RequiredArgsConstructor
public class AuthUser implements Serializable {

  @Serial
  private static final long serialVersionUID = -4398415164825744294L;

  private final String login;
  private final String password;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean activated;
  private final String langKey;
  private final String activationKey;
  private final String resetKey;
  private final LocalDateTime resetDate;
  private final boolean welcomeDialogDeactivated;
}
