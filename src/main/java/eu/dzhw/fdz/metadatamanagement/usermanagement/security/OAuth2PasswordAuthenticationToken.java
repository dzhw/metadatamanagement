package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;

/**
 * Repräsentiert die Attribute, die beim Anmelden vom Frontend geschickt werden.
 */
public class OAuth2PasswordAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

  private final String username;
  private final String password;

  public OAuth2PasswordAuthenticationToken(Authentication clientPrincipal,
                                           Map<String, Object> additionalParameters) {
    super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);
    this.username = (String) additionalParameters.get("username");
    this.password = (String) additionalParameters.get("password");
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
