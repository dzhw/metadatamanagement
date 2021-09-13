/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class ConstantsTest {
  @Test
  public void testConstants()
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Arrange
    Constructor<Constants> constructor = Constants.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Constants constants = constructor.newInstance();

    // Act

    // Assert
    assertThat(constants, not(nullValue()));
    assertThat(Constants.SPRING_PROFILE_LOCAL, is("local"));
    assertThat(Constants.SPRING_PROFILE_DEVELOPMENT, is("dev"));
    assertThat(Constants.SYSTEM_ACCOUNT, is("system"));
  }
}
