package eu.dzhw.fdz.metadatamanagement.authmanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Test class for the SecurityUtils utility class.
 *
 * @author Daniel Katzberg
 * @see SecurityUtils
 */
public class SecurityUtilsTest {

  @AfterEach
  public void logout() {
    SecurityContextHolder.clearContext();
  }

  @Test
  public void testConstructor()
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Arrange
    Constructor<SecurityUtils> constructor = SecurityUtils.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    SecurityUtils securityUtils = constructor.newInstance();

    // Act

    // Assert
    assertThat(securityUtils, not(nullValue()));
  }

  @Test
  public void getCurrentUserLoginTest() {

    // Arrange
    addJwtAuthenticationContext();

    // Act
    String login = SecurityUtils.getCurrentUserLogin();

    // Assert
    assertThat(login).isEqualTo("admin");
    assertThat(SecurityUtils.isUserInRole("somethingWrong"), is(false));
    assertThat(SecurityUtils.isUserInRole("admin"), is(true));
  }

  @Test
  public void isUserAnonymousTest() {
    assertThat(SecurityUtils.isUserAnonymous(), is(true));

    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new AnonymousAuthenticationToken(
        "test",
        "test",
        List.of(new SimpleGrantedAuthority("authenticated"))
    ));
    SecurityContextHolder.setContext(context);

    assertThat(SecurityUtils.isUserAnonymous(), is(true));

    addJwtAuthenticationContext();

    assertThat(SecurityUtils.isUserAnonymous(), is(false));
  }

  @Test
  public void isUserInRole() {
    assertThat(SecurityUtils.isUserInRole("admin"), is(false));

    addJwtAuthenticationContext();
    assertThat(SecurityUtils.isUserInRole("admin"), is(true));
  }

  private void addJwtAuthenticationContext() {
    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new JwtAuthenticationToken(
      new Jwt(
        "test_token",
        Instant.now(),
        Instant.now().plusSeconds(5),
        Map.of("alg", "RS256", "typ", "JWT"),

        Map.of("sub", "admin", "scope", new String[]{"admin"})
      ),
      List.of(new SimpleGrantedAuthority("admin"))
    ));
    SecurityContextHolder.setContext(context);
  }
}
