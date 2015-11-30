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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

/**
 * No Integration Test. No need for application Context.
 * @author Daniel Katzberg 
 */
public class CachingHttpHeadersFilterTest {
  @Test
  public void testInit() throws ServletException {
    // Arrange
    CachingHttpHeadersFilter filter = this.createDefaultCachingHttpHeadersFilter();

    // Act
    filter.init(Mockito.mock(FilterConfig.class));// Nothing happens.

    // Assert
    assertThat(filter, not(nullValue()));
  }

  @Test
  public void testDestroy() {
    // Arrange
    CachingHttpHeadersFilter filter = this.createDefaultCachingHttpHeadersFilter();

    // Act
    filter.destroy(); // Nothing happens.

    // Assert
    assertThat(filter, not(nullValue()));
  }

  @Test
  public void testDoFilter() throws IOException, ServletException {
    // Arrange
    CachingHttpHeadersFilter filter = this.createDefaultCachingHttpHeadersFilter();
    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    FilterChain chain = Mockito.mock(FilterChain.class);
    
    // Act
    filter.doFilter(request, response, chain);

    // Assert
    assertThat(filter, not(nullValue()));
  }
  
  private CachingHttpHeadersFilter createDefaultCachingHttpHeadersFilter() {
    Environment environment = Mockito.mock(Environment.class);
    when(environment.getProperty("jhipster.http.cache.timeToLiveInDays",
        Long.class, 31L)).thenReturn(2L);
    return new CachingHttpHeadersFilter(environment);
  }
}
