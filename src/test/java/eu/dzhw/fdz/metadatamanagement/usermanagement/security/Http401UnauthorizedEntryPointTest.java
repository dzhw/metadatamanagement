/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.AuthenticationException;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.Http401UnauthorizedEntryPoint;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class Http401UnauthorizedEntryPointTest {

  @Test
  public void testCommence() throws IOException, ServletException {
    // Arrange
    Http401UnauthorizedEntryPoint entryPoint = new Http401UnauthorizedEntryPoint();

    // Act
    entryPoint.commence(Mockito.mock(HttpServletRequest.class),
        Mockito.mock(HttpServletResponse.class), new AuthenticationException("message") {
          private static final long serialVersionUID = 1L;
        });

    // Assert
    assertThat(entryPoint, not(nullValue()));
  }

}
