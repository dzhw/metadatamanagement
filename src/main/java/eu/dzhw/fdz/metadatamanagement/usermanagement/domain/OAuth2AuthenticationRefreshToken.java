package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The oauth2 refresh token.
 */
@Document(collection = "oauth2_authentication_refresh_tokens")
@SuppressFBWarnings(value = { "EI_EXPOSE_REP", "EI_EXPOSE_REP2" })
public class OAuth2AuthenticationRefreshToken {

  @Id
  private String tokenId;
  private byte[] token;
  private byte[] authentication;

  public OAuth2AuthenticationRefreshToken() {}

  /**
   * Create the refresh token.
   */
  @PersistenceConstructor
  public OAuth2AuthenticationRefreshToken(final String tokenId, final byte[] token,
      final byte[] authentication) {
    this.tokenId = tokenId;
    this.token = token;
    this.authentication = authentication;
  }

  public String getTokenId() {
    return tokenId;
  }

  public byte[] getToken() {
    return token;
  }

  public byte[] getAuthentication() {
    return authentication;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {      
      return true;
    }

    if (!(other instanceof OAuth2AuthenticationRefreshToken)) {      
      return false;
    }

    OAuth2AuthenticationRefreshToken that = (OAuth2AuthenticationRefreshToken) other;

    return new EqualsBuilder().append(tokenId, that.tokenId).append(token, that.token)
        .append(authentication, that.authentication).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(tokenId).append(token).append(authentication)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("tokenId", tokenId).append("token", token)
        .append("authentication", authentication).toString();
  }
}
