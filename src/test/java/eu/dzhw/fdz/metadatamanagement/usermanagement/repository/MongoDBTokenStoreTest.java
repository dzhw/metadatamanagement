/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * @author Daniel Katzberg
 *
 */
public class MongoDBTokenStoreTest extends AbstractTest {

  @Autowired
  private OAuth2AccessTokenRepository oAuth2AccessTokenRepository;

  @Autowired
  private OAuth2RefreshTokenRepository oAuth2RefreshTokenRepository;

  private MongoDbTokenStore mongoDBTokenStore;
  private DefaultOAuth2AccessToken accessToken;
  private OAuth2RefreshToken refreshToken;
  private OAuth2Authentication authentication;

  @Before
  public void beforeEachTest() {
    this.mongoDBTokenStore =
        new MongoDbTokenStore(oAuth2AccessTokenRepository, oAuth2RefreshTokenRepository);

    OAuth2Request storedRequest = new OAuth2Request(new HashMap<>(), "clientId", new ArrayList<>(),
        true, new HashSet<>(), new HashSet<>(), "/", new HashSet<>(), new HashMap<>());
    UsernamePasswordAuthenticationToken userAuthentication =
        new UsernamePasswordAuthenticationToken("principal",
            new UsernamePasswordCredentials("username", "password"));
    this.authentication = new OAuth2Authentication(storedRequest, userAuthentication);

    this.refreshToken = new DefaultOAuth2RefreshToken("refreshToken");
    this.accessToken = new DefaultOAuth2AccessToken("accessToken");
    this.accessToken.setRefreshToken(this.refreshToken);
    this.mongoDBTokenStore.storeAccessToken(this.accessToken, this.authentication);
    this.mongoDBTokenStore.storeRefreshToken(this.refreshToken, this.authentication);
  }

  @After
  public void afterEachTest() {
    this.mongoDBTokenStore.removeAccessToken(this.accessToken);
    this.mongoDBTokenStore.removeRefreshToken(this.refreshToken);
  }

  @Test
  public void testReadAuthentication() {
    // Arrange

    // Act
    OAuth2Authentication auth2Authentication =
        this.mongoDBTokenStore.readAuthentication(this.accessToken);

    // Assert
    assertThat(auth2Authentication, not(nullValue()));
    assertThat(auth2Authentication.getName(), is("principal"));
    assertThat(auth2Authentication.getPrincipal(), is("principal"));
  }

  @Test
  public void testReadAccessToken() {
    // Arrange

    // Act
    OAuth2AccessToken accessToken =
        this.mongoDBTokenStore.readAccessToken(this.accessToken.getValue());

    // Assert
    assertThat(accessToken, not(nullValue()));
    assertThat(accessToken, is(this.accessToken));
    assertThat(accessToken.getRefreshToken(), is(this.refreshToken));
  }

  @Test
  public void testReadRefreshToken() {
    // Arrange

    // Act
    OAuth2RefreshToken refreshToken =
        this.mongoDBTokenStore.readRefreshToken(this.refreshToken.getValue());

    // Assert
    assertThat(refreshToken, not(nullValue()));
    assertThat(refreshToken, is(this.refreshToken));
  }

  @Test
  public void testReadAuthenticationForRefreshToken() {
    // Arrange

    // Act
    OAuth2Authentication auth2Authentication =
        this.mongoDBTokenStore.readAuthenticationForRefreshToken(this.refreshToken);

    // Assert
    assertThat(auth2Authentication, not(nullValue()));
    assertThat(auth2Authentication.getName(), is("principal"));
    assertThat(auth2Authentication.getPrincipal(), is("principal"));
  }

  @Test
  public void testRemoveAccessTokenUsingRefreshToken() {
    // Arrange

    // Act
    this.mongoDBTokenStore.removeAccessTokenUsingRefreshToken(this.refreshToken);

    // Assert
    assertThat(this.mongoDBTokenStore.readAccessToken(this.accessToken.getValue()),
        is(nullValue()));
  }

  @Test
  public void testGetAccessToken() {
    // Arrange

    // Act
    OAuth2AccessToken accessToken = this.mongoDBTokenStore.getAccessToken(this.authentication);

    // Assert
    assertThat(accessToken, not(nullValue()));
    assertThat(accessToken, is(this.accessToken));
    assertThat(accessToken.getRefreshToken(), is(this.refreshToken));
  }

  @Test
  public void testFindTokensByClientId() {
    // Arrange

    // Act
    Collection<OAuth2AccessToken> accessTokens =
        this.mongoDBTokenStore.findTokensByClientId("clientId");

    // Assert
    assertThat(accessTokens, not(nullValue()));
    assertThat(accessTokens.size(), is(1));
    assertThat(accessTokens.contains(this.accessToken), is(true));
  }

  @Test
  public void testFindTokensByClientIdAndUserName() {
    // Arrange

    // Act
    Collection<OAuth2AccessToken> accessTokens =
        this.mongoDBTokenStore.findTokensByClientIdAndUserName("clientId", "principal");

    // Assert
    assertThat(accessTokens, not(nullValue()));
    assertThat(accessTokens.size(), is(1));
    assertThat(accessTokens.contains(this.accessToken), is(true));
  }
}
