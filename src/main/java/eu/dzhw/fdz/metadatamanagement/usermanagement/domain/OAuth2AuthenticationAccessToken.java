package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The OAuth2 AccessToken.
 */
@Document(collection = "oauth2_authentication_access_tokens")
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class OAuth2AuthenticationAccessToken {

  @Id
  private String tokenId;

  private byte[] token;

  private String authenticationId;

  private String username;

  private String clientId;

  private byte[] authentication;

  private String refreshToken;


  public OAuth2AuthenticationAccessToken() {}

  /**
   * Construct an access token.
   */
  @PersistenceConstructor
  public OAuth2AuthenticationAccessToken(final String tokenId, final byte[] token,
      final String authenticationId, final String username, final String clientId,
      final byte[] authentication, final String refreshToken) {
    this.tokenId = tokenId;
    this.token = token;
    this.authenticationId = authenticationId;
    this.username = username;
    this.clientId = clientId;
    this.authentication = authentication;
    this.refreshToken = refreshToken;
  }

  public String getTokenId() {
    return tokenId;
  }

  public byte[] getToken() {
    return token;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  public String getUsername() {
    return username;
  }

  public String getClientId() {
    return clientId;
  }

  public byte[] getAuthentication() {
    return authentication;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, authenticationId, username, clientId, authentication, refreshToken);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final OAuth2AuthenticationAccessToken other = (OAuth2AuthenticationAccessToken) obj;
    return this.token == other.token
        && Objects.equals(this.authenticationId, other.authenticationId)
        && Objects.equals(this.username, other.username)
        && Objects.equals(this.clientId, other.clientId)
        && this.authentication == other.authentication
        && Objects.equals(this.refreshToken, other.refreshToken);
  }
}
