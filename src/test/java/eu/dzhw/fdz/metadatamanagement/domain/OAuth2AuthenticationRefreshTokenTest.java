/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

/**
 * @author Daniel Katzberg
 *
 */
public class OAuth2AuthenticationRefreshTokenTest {

  @Test
  public void testOAuth2AuthenticationAccessToken() {
    // Arrange
    OAuth2AuthenticationRefreshToken token = this.createRefreshToken();

    // Act

    // Assert
    assertThat(token.getTokenId(), is("RefreshTokenValue"));
  }

  @Test
  public void testEquals() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
      IllegalAccessException {

    // Arrange
    OAuth2AuthenticationRefreshToken token1 = this.createRefreshToken();
    OAuth2AuthenticationRefreshToken token2 = this.createRefreshToken();

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
    OAuth2AuthenticationRefreshToken token = this.createRefreshToken();
    OAuth2AuthenticationRefreshToken tokenWithoutId = this.createRefreshToken();

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

  private OAuth2AuthenticationRefreshToken createRefreshToken() {

    // Arrange
    OAuth2RefreshToken refreshToken = Mockito.mock(OAuth2RefreshToken.class);
    when(refreshToken.getValue()).thenReturn("RefreshTokenValue");
    OAuth2Authentication auth2Authentication = Mockito.mock(OAuth2Authentication.class);
    OAuth2AuthenticationRefreshToken token =
        new OAuth2AuthenticationRefreshToken(refreshToken, auth2Authentication);

    // Act

    // Assert
    assertThat(token.getAuthentication(), is(auth2Authentication));
    assertThat(token.getoAuth2RefreshToken(), is(refreshToken));

    return token;
  }
  
}
