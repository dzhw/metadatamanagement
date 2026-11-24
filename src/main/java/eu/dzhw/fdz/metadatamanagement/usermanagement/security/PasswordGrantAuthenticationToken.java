package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import java.util.Collections;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

public final class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

  public static final AuthorizationGrantType GRANT_TYPE = new AuthorizationGrantType("password");

  private final String username;
  private final String password;
  private final Set<String> scopes;

  public PasswordGrantAuthenticationToken(Authentication clientPrincipal, String username, String password,
      Set<String> scopes) {
    super(GRANT_TYPE, clientPrincipal, Collections.emptyMap());
    this.username = username;
    this.password = password;
    this.scopes = Set.copyOf(scopes);
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Set<String> getScopes() {
    return scopes;
  }
}