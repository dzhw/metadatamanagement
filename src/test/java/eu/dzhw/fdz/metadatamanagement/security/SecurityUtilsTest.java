package eu.dzhw.fdz.metadatamanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestUtils;

/**
 * Test class for the SecurityUtils utility class.
 *
 * @author Daniel Katzberg
 * @see SecurityUtils
 */
public class SecurityUtilsTest {

  @After
  public void logout() {
    UnitTestUtils.logout();
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
  public void testGetCurrentUser() {

    // Arrange
    Set<GrantedAuthority> set = new HashSet<>();
    set.add(new SimpleGrantedAuthority("testAuthority"));
    CustomUserDetails login =
        new CustomUserDetails("id", "username", "user", set, true, true, true, true);
    UnitTestUtils.login(login, "user");

    // Act

    // Assert
    assertThat(SecurityUtils.getCurrentUserId(), is("id"));
    assertThat(SecurityUtils.isUserInRole("testAuthority"), is(true));
    assertThat(SecurityUtils.isUserInRole("somethingWrong"), is(false));
    assertThat(SecurityUtils.getCurrentUserLogin(), is("username"));
  }

  @Test
  public void testgetCurrentUserLogin() {
    // Arrange
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);

    // Act
    String login = SecurityUtils.getCurrentUserLogin();

    // Assert
    assertThat(login).isEqualTo("admin");
    assertThat(SecurityUtils.isUserInRole("somethingWrong"), is(false));
  }

  @Test(expected = IllegalStateException.class)
  public void testgetCurrentUserWithException() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    SecurityUtils.getCurrentUser();// Exception is here. No CustomUserDetails;
  }

  @Test
  public void testIsAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("admin", "admin"));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtils.isAuthenticated();
    assertThat(isAuthenticated).isTrue();
  }

  @Test
  public void testAnonymousIsNotAuthenticated() {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken("anonymous", "anonymous", authorities));
    SecurityContextHolder.setContext(securityContext);
    boolean isAuthenticated = SecurityUtils.isAuthenticated();
    assertThat(isAuthenticated).isFalse();
  }
}
