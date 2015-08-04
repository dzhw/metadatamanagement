package eu.dzhw.fdz.metadatamanagement.web.https;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.config.https.HttpsRedirectFilter;
import eu.dzhw.fdz.metadatamanagement.web.util.UrlHelper;

/**
 * Unit Test for the {@link HttpsRedirectFilter} which is only available when runnning on pivotal
 * cloudfoundry.
 * 
 * @author René Reitmann
 */
public class HttpsRedirectFilterTest {

  private HttpsRedirectFilter httpsRedirectFilter;

  @Before
  public void setup() throws ServletException {
    httpsRedirectFilter = new HttpsRedirectFilter(new UrlHelper());
    httpsRedirectFilter.init(null);
  }

  @After
  public void tearDown() throws ServletException {
    httpsRedirectFilter.destroy();
  }

  @Test(expected = IllegalStateException.class)
  public void testRedirectFromHttpToHttpsWithoutProtocolHeader() throws Exception {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    // mock the getRequestURL() response
    when(httpServletRequest.getRequestURL())
        .thenReturn(new StringBuffer("http://metadatamanagement.cfapps.io/"));
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    // this should throw an exception cause the protocol header is not present
    httpsRedirectFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
  }

  @Test
  public void testRedirectFromHttpToHttpsWithProtocolHeader() throws IOException, ServletException {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    // mock the getRequestURL() response
    when(httpServletRequest.getRequestURL())
        .thenReturn(new StringBuffer("http://metadatamanagement.cfapps.io/"));
    // mock the getHeader() response
    when(httpServletRequest.getHeader("X-Forwarded-Proto")).thenReturn("http");
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    httpsRedirectFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

    // verify if a sendRedirect() was performed with the expected value
    verify(httpServletResponse).sendRedirect("https://metadatamanagement.cfapps.io/");
  }

  @Test
  public void testNotRedirectedIfAlreadyHttps() throws IOException, ServletException {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    // mock the getRequestURL() response
    when(httpServletRequest.getRequestURL())
        .thenReturn(new StringBuffer("http://metadatamanagement.cfapps.io/"));
    // mock the getHeader() response
    when(httpServletRequest.getHeader("X-Forwarded-Proto")).thenReturn("https");
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    httpsRedirectFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

    // verify if a sendRedirect() was performed with the expected value
    verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
  }

}
