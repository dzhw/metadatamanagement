/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import eu.dzhw.fdz.metadatamanagement.usermanagement.domain.OAuth2AuthenticationAccessToken;

/**
 * @author Daniel Katzberg
 *
 */
public class OAuth2AuthenticationAccessTokenTest {

  @Test
  public void testOAuth2AuthenticationAccessToken() {
    // Arrange
    OAuth2AuthenticationAccessToken token = this.createAccessToken();

    // Act

    // Assert
    assertThat(token.getAuthenticationId(), is("AuthenticationID"));
    assertThat(token.getTokenId(), is("Value"));
    assertThat(token.getUserName(), is("Name"));
    assertThat(token.getClientId(), is(nullValue()));
    assertThat(token.getRefreshToken(), is("RefreshTokenValue"));
  }

  @Test
  public void testEquals() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {

    // Arrange
    OAuth2AuthenticationAccessToken token1 = this.createAccessToken();
    OAuth2AuthenticationAccessToken token2 = this.createAccessToken();

    // Act
    boolean checkNull = token1.equals(null);
    boolean checkClass = token1.equals(new Object());
    boolean checkSame = token1.equals(token1);
    boolean checkDifferent = token1.equals(token2);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
  }

  @Test
  public void testHashCode() throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {

    // Arrange
    OAuth2AuthenticationAccessToken token = this.createAccessToken();
    OAuth2AuthenticationAccessToken tokenWithoutId = this.createAccessToken();

    Field idField = tokenWithoutId.getClass()
      .getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(tokenWithoutId, null);

    // Act
    int hashCode = token.hashCode();
    int hashCodeWithoutId = tokenWithoutId.hashCode();

    // Assert
    assertThat(hashCode, not(0));
    assertThat(hashCodeWithoutId, is(0));
  }

  private OAuth2AuthenticationAccessToken createAccessToken() {

    // Arrange
    OAuth2AccessToken accessToken = Mockito.mock(OAuth2AccessToken.class);
    when(accessToken.getValue()).thenReturn("Value");
    OAuth2RefreshToken refreshToken = Mockito.mock(OAuth2RefreshToken.class);
    when(accessToken.getRefreshToken()).thenReturn(refreshToken);
    when(refreshToken.getValue()).thenReturn("RefreshTokenValue");
    OAuth2Authentication auth2Authentication = Mockito.mock(OAuth2Authentication.class);
    when(auth2Authentication.getName()).thenReturn("Name");
    OAuth2Request oAuth2Request = Mockito.mock(OAuth2Request.class);
    when(auth2Authentication.getOAuth2Request()).thenReturn(oAuth2Request);
    String authenticationId = "AuthenticationID";
    OAuth2AuthenticationAccessToken token =
        new OAuth2AuthenticationAccessToken(accessToken, auth2Authentication, authenticationId);

    // Act

    // Assert
    assertThat(token.getAuthentication(), is(auth2Authentication));
    assertThat(token.getoAuth2AccessToken(), is(accessToken));

    return token;
  }

}
