package eu.dzhw.fdz.metadatamanagement.web.i18n;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.config.i18n.PathLocaleResolver;

/**
 * Unit Test for the {@link PathLocaleResolver}.
 * 
 * @author René Reitmann
 */
public class PathLocaleResolverTest {

  private PathLocaleResolver pathLocaleResolver;

  @Before
  public void setUp() {
    pathLocaleResolver = new PathLocaleResolver();
  }

  @Test
  public void testGermanLocaleExtractedFromPathParam() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    when(httpServletRequest.getRequestURI())
        .thenReturn("http://metadatamanagement.cfapps.io/de/variables/search");
    when(httpServletRequest.getContextPath()).thenReturn("http://metadatamanagement.cfapps.io");

    pathLocaleResolver.resolveLocale(httpServletRequest);

    // verify the right locale was set as request attribute
    verify(httpServletRequest).setAttribute(PathLocaleResolver.LOCALE_ATTRIBUTE, Locale.GERMAN);
  }

  @Test
  public void testGermanLocaleExtractedFromAcceptHeader() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    when(httpServletRequest.getRequestURI()).thenReturn("http://metadatamanagement.cfapps.io");
    when(httpServletRequest.getContextPath()).thenReturn("http://metadatamanagement.cfapps.io");
    when(httpServletRequest.getLocales())
        .thenReturn(Collections.enumeration(Arrays.asList(Locale.GERMAN)));

    pathLocaleResolver.resolveLocale(httpServletRequest);

    // verify the right locale was set as request attribute
    verify(httpServletRequest).setAttribute(PathLocaleResolver.LOCALE_ATTRIBUTE, Locale.GERMAN);
  }

  @Test
  public void testDefaultLocaleExtracted() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

    when(httpServletRequest.getRequestURI())
        .thenReturn("http://metadatamanagement.cfapps.io/variables/search");
    when(httpServletRequest.getContextPath()).thenReturn("http://metadatamanagement.cfapps.io");
    when(httpServletRequest.getLocales()).thenReturn(Collections.emptyEnumeration());

    pathLocaleResolver.resolveLocale(httpServletRequest);

    // verify the right locale was set as request attribute
    verify(httpServletRequest).setAttribute(PathLocaleResolver.LOCALE_ATTRIBUTE, Locale.ENGLISH);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsopportedOperation() {
    // create the objects to be mocked
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

    pathLocaleResolver.setLocale(httpServletRequest, httpServletResponse, Locale.GERMAN);;
  }
}
