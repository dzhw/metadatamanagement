/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

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
 */
public class ErrorConstantsTest {

  @Test
  public void testErrorConstants()
      throws NoSuchMethodException, SecurityException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    // Arrange
    Constructor<ErrorConstants> constructor = ErrorConstants.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    ErrorConstants errorConstants = constructor.newInstance();

    // Act

    // Assert
    assertThat(errorConstants, not(nullValue()));
    assertThat(ErrorConstants.ERR_ACCESS_DENIED, is("error.accessDenied"));
    assertThat(ErrorConstants.ERR_CONCURRENCY_FAILURE, is("error.concurrencyFailure"));
    assertThat(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, is("error.methodNotSupported"));
    assertThat(ErrorConstants.ERR_VALIDATION, is("error.validation"));
  }

}
