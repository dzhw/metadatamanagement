package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * The oauth2 refresh token.
 */
@Document(collection = "OAUTH_AUTHENTICATION_REFRESH_TOKEN")
public class OAuth2AuthenticationRefreshToken {

  @Id
  private String id;

  private String tokenId;

  private OAuth2RefreshToken oauth2RefreshToken;

  private OAuth2Authentication authentication;

  /**
   * Create the refresh token.
   */
  public OAuth2AuthenticationRefreshToken(OAuth2RefreshToken oauth2RefreshToken,
      OAuth2Authentication authentication) {
    this.id = UUID.randomUUID()
      .toString();
    this.oauth2RefreshToken = oauth2RefreshToken;
    this.authentication = authentication;
    this.tokenId = oauth2RefreshToken.getValue();
  }

  public String getTokenId() {
    return tokenId;
  }

  public OAuth2RefreshToken getoAuth2RefreshToken() {
    return oauth2RefreshToken;
  }

  public OAuth2Authentication getAuthentication() {
    return authentication;
  }


  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;      
    }
    if (object == null || getClass() != object.getClass()) {
      return false;      
    }

    OAuth2AuthenticationRefreshToken that = (OAuth2AuthenticationRefreshToken) object;

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
