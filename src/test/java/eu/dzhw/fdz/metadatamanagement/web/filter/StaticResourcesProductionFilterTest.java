/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.filter;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Daniel Katzberg 
 * No Integration Test. No need for application Context.
 */
public class StaticResourcesProductionFilterTest {

  @Test
  public void testInit() throws ServletException {
    // Arrange
    StaticResourcesProductionFilter filter = new StaticResourcesProductionFilter();

    // Act
    filter.init(Mockito.mock(FilterConfig.class));// Nothing happens.

    // Assert
    assertThat(filter, not(nullValue()));
  }

  @Test
  public void testDestroy() {
    // Arrange
    StaticResourcesProductionFilter filter = new StaticResourcesProductionFilter();

    // Act
    filter.destroy(); // Nothing happens.

    // Assert
    assertThat(filter, not(nullValue()));
  }

  @Test
  public void testDoFilter() throws IOException, ServletException {
    // Arrange
    StaticResourcesProductionFilter filter = new StaticResourcesProductionFilter();
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    FilterChain chain = Mockito.mock(FilterChain.class);
    when(request.getContextPath()).thenReturn("");
    when(request.getRequestURI()).thenReturn("/");
    when(request.getRequestDispatcher("/dist/index.html"))
      .thenReturn(Mockito.mock(RequestDispatcher.class));

    // Act
    filter.doFilter(request, response, chain);

    // Assert
    assertThat(filter, not(nullValue()));
  }

  @Test
  public void testDoFilterWithWrongRequest() throws IOException, ServletException {
    // Arrange
    StaticResourcesProductionFilter filter = new StaticResourcesProductionFilter();
    ServletRequestWrapper request = Mockito.mock(ServletRequestWrapper.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    FilterChain chain = Mockito.mock(FilterChain.class);

    // Act
    filter.doFilter(request, response, chain); // should break up

    // Assert
    assertThat(filter, not(nullValue()));
  }


}
