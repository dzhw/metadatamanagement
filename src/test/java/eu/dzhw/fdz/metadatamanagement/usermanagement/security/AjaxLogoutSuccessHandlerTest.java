/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.security;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;

/**
 * @author Daniel Katzberg
 *
 */
public class AjaxLogoutSuccessHandlerTest extends AbstractTest {

  @Autowired
  private AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

  @Test
  public void testOnLogoutSuccess() throws IOException, ServletException {
    // Arrange
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    Authentication authentication = Mockito.mock(Authentication.class);
    when(request.getHeader("authorization")).thenReturn("Bearer user");

    // Act
    this.ajaxLogoutSuccessHandler.onLogoutSuccess(request, response, authentication);

    // Assert
    assertThat(ajaxLogoutSuccessHandler, not(nullValue()));
  }

}
