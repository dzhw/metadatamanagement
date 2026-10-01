package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "oauth2_authorizations")
public class OAuth2AuthorizationDocument {

  @Id 
  private String id;
  private String registeredClientId;
  private String principalName;
  private String authorizationGrantType;
  private Set<String> authorizedScopes;
  private Set<String> principalAuthorities;
  private String accessTokenValue;
  private Instant accessTokenIssuedAt;
  private Instant accessTokenExpiresAt;
  private boolean accessTokenInvalidated;
  private String refreshTokenValue;
  private Instant refreshTokenIssuedAt;
  private Instant refreshTokenExpiresAt;
  private boolean refreshTokenInvalidated;
  private String state;
  private Map<String, Object> attributes = new HashMap<>();

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getRegisteredClientId() {
    return registeredClientId;
  }
  public void setRegisteredClientId(String registeredClientId) {
    this.registeredClientId = registeredClientId;
  }
  public String getPrincipalName() {
    return principalName;
  }
  public void setPrincipalName(String principalName) {
    this.principalName = principalName;
  }
  public String getAuthorizationGrantType() {
    return authorizationGrantType;
  }
  public void setAuthorizationGrantType(String authorizationGrantType) {
    this.authorizationGrantType = authorizationGrantType;
  }
  public Set<String> getAuthorizedScopes() {
    return authorizedScopes;
  }
  public void setAuthorizedScopes(Set<String> authorizedScopes) {
    this.authorizedScopes = authorizedScopes;
  }
  public Set<String> getPrincipalAuthorities() {
    return principalAuthorities;
  }
  public void setPrincipalAuthorities(Set<String> principalAuthorities) {
    this.principalAuthorities = principalAuthorities;
  }
  public String getAccessTokenValue() {
    return accessTokenValue;
  }
  public void setAccessTokenValue(String accessTokenValue) {
    this.accessTokenValue = accessTokenValue;
  }
  public Instant getAccessTokenIssuedAt() {
    return accessTokenIssuedAt;
  }
  public void setAccessTokenIssuedAt(Instant accessTokenIssuedAt) {
    this.accessTokenIssuedAt = accessTokenIssuedAt;
  }
  public Instant getAccessTokenExpiresAt() {
    return accessTokenExpiresAt;
  }
  public void setAccessTokenExpiresAt(Instant accessTokenExpiresAt) {
    this.accessTokenExpiresAt = accessTokenExpiresAt;
  }
  public boolean isAccessTokenInvalidated() {
    return accessTokenInvalidated;
  }
  public void setAccessTokenInvalidated(boolean accessTokenInvalidated) {
    this.accessTokenInvalidated = accessTokenInvalidated;
  }
  public String getRefreshTokenValue() {
    return refreshTokenValue;
  }
  public void setRefreshTokenValue(String refreshTokenValue) {
    this.refreshTokenValue = refreshTokenValue;
  }
  public Instant getRefreshTokenIssuedAt() {
    return refreshTokenIssuedAt;
  }
  public void setRefreshTokenIssuedAt(Instant refreshTokenIssuedAt) {
    this.refreshTokenIssuedAt = refreshTokenIssuedAt;
  }
  public Instant getRefreshTokenExpiresAt() {
    return refreshTokenExpiresAt;
  }
  public void setRefreshTokenExpiresAt(Instant refreshTokenExpiresAt) {
    this.refreshTokenExpiresAt = refreshTokenExpiresAt;
  }
  public boolean isRefreshTokenInvalidated() {
    return refreshTokenInvalidated;
  }
  public void setRefreshTokenInvalidated(boolean refreshTokenInvalidated) {
    this.refreshTokenInvalidated = refreshTokenInvalidated;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }
  public Map<String, Object> getAttributes() {
    return attributes;
  }
  public void setAttributes(Map<String, Object> attributes) {
    this.attributes = attributes;
  }
}
