package eu.dzhw.fdz.metadatamanagement.authmanagement.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestUserManagementUtils;

/**
 * Test class for the SecurityUtils utility class.
 *
 * @author Daniel Katzberg
 * @see SecurityUtils
 */
public class SecurityUtilsTest {

  @AfterEach
  public void logout() {
    UnitTestUserManagementUtils.logout();
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
}
