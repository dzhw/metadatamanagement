package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The OAuth2 AccessToken.
 */
@Document(collection = "OAUTH_AUTHENTICATION_ACCESS_TOKEN")
public class OAuth2AuthenticationAccessToken implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  private String tokenId;

  @SuppressFBWarnings("SE_BAD_FIELD") // non transient, no serializable field
  private OAuth2AccessToken oauth2AccessToken;

  private String authenticationId;

  private String userName;

  private String clientId;

  private OAuth2Authentication authentication;

  private String refreshToken;

  /**
   * Construct an access token.
   */
  @PersistenceConstructor
  public OAuth2AuthenticationAccessToken(OAuth2AccessToken oauth2AccessToken,
      OAuth2Authentication authentication, String authenticationId) {
    this.id = UUID.randomUUID()
      .toString();
    this.tokenId = oauth2AccessToken.getValue();
    this.oauth2AccessToken = oauth2AccessToken;
    this.authenticationId = authenticationId;
    this.userName = authentication.getName();
    this.clientId = authentication.getOAuth2Request()
      .getClientId();
    this.authentication = authentication;
    this.refreshToken = oauth2AccessToken.getRefreshToken()
      .getValue();
  }

  public String getTokenId() {
    return tokenId;
  }

  public OAuth2AccessToken getoAuth2AccessToken() {
    return oauth2AccessToken;
  }

  public String getAuthenticationId() {
    return authenticationId;
  }

  @SuppressFBWarnings("NM_CONFUSING")
  public String getUserName() {
    return userName;
  }

  public String getClientId() {
    return clientId;
  }

  public OAuth2Authentication getAuthentication() {
    return authentication;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;      
    }
    if (object == null || getClass() != object.getClass()) {
      return false;      
    }

    OAuth2AuthenticationAccessToken that = (OAuth2AuthenticationAccessToken) object;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;      
    }

    return true;
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
