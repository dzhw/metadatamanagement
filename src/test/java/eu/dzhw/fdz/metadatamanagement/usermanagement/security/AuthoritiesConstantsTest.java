/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class AuthoritiesConstantsTest {


  @Test
  public void testAuthoritiesConstants()
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Arrange
    Constructor<AuthoritiesConstants> constructor =
        AuthoritiesConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    AuthoritiesConstants authoritiesConstants = constructor.newInstance();

    // Act

    // Assert
    assertThat(authoritiesConstants, not(nullValue()));
    assertThat(AuthoritiesConstants.ADMIN, is("ROLE_ADMIN"));
    assertThat(AuthoritiesConstants.USER, is("ROLE_USER"));
    assertThat(AuthoritiesConstants.ANONYMOUS, is("ROLE_ANONYMOUS"));
  }
}
