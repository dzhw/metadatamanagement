package eu.dzhw.fdz.metadatamanagement.web.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test for the {@link UrlHelper}.
 * 
 * @author René Reitmann
 */
public class UrlHelperTest {

  private UrlHelper urlHelper;

  @Before
  public void setup() throws ServletException {
    urlHelper = new UrlHelper();
  }

  @Test
  public void testWithQueryParam() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    // mock the getRequestURL() response
    when(httpServletRequest.getRequestURL())
        .thenReturn(new StringBuffer("http://metadatamanagement.cfapps.io/de/variables/search"));
    // mock the getQueryString() response
    when(httpServletRequest.getQueryString()).thenReturn("query=hurz");

    // invoke method to test
    String securedUrl = urlHelper.getSecureUrl(httpServletRequest);
    assertThat(securedUrl,
        is("https://metadatamanagement.cfapps.io/de/variables/search?query=hurz"));
  }

  @Test
  public void testWithoutQueryParam() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    // mock the getRequestURL() response
    when(httpServletRequest.getRequestURL())
        .thenReturn(new StringBuffer("http://metadatamanagement.cfapps.io/de/variables/search"));

    // invoke method to test
    String securedUrl = urlHelper.getSecureUrl(httpServletRequest);
    assertThat(securedUrl, is("https://metadatamanagement.cfapps.io/de/variables/search"));
  }

}
