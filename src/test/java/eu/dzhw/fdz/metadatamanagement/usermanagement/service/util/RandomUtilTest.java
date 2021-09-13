/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.service.util;

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
public class RandomUtilTest {

  @Test
  public void testConstructor()
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Arrange
    Constructor<RandomUtil> constructor = RandomUtil.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    RandomUtil randomUtil = constructor.newInstance();

    // Act

    // Assert
    assertThat(randomUtil, not(nullValue()));
  }

  @Test
  public void testGeneratePassword() {
    // Arrange
    String password = RandomUtil.generatePassword();
    
    // Act
    
    // Assert
    assertThat(password.length(), is(20));
  }

}
