/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * 
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class UserNotActivatedExceptionTest {

  @Test
  public void testUserNotActivatedException() {
    // Arrange
    UserNotActivatedException activatedException =
        new UserNotActivatedException("message", Mockito.mock(Throwable.class));

    // Act

    // Assert
    assertThat(activatedException, not(nullValue()));
    assertThat(activatedException.getMessage(), is("message"));
  }

}
