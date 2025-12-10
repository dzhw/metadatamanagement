/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.config.locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * @author Daniel Katzberg
 *
 */
public class AngularCookieLocaleResolverTest {

  @Test
  public void testResolveLocale() {
    // Arrange
    AngularCookieLocaleResolver resolver = new AngularCookieLocaleResolver();
    HttpServletRequest servlet = Mockito.mock(HttpServletRequest.class);
    when(servlet.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME))
      .thenReturn(Locale.GERMAN);

    // Act
    Locale locale = resolver.resolveLocale(servlet);

    // Assert
    assertThat(locale, is(Locale.GERMAN));
  }

  @Test
  public void testResolveLocaleContext() {
    // Arrange
    AngularCookieLocaleResolver resolver = new AngularCookieLocaleResolver();
    HttpServletRequest servlet = Mockito.mock(HttpServletRequest.class);
    when(servlet.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME))
      .thenReturn(Locale.GERMAN);

    // Act
    LocaleContext locale =
        resolver.resolveLocaleContext(servlet);

    // Assert
    assertThat(locale.getLocale(), is(Locale.GERMAN));
  }

//  TODO This test doesn't work that way anymore because CookieLocaleResolver changed and it's unclear what this test was supposed to prove.
//  @Test
//  public void testAddCookie() {
//    // Arrange
//    AngularCookieLocaleResolver resolver = new AngularCookieLocaleResolver();
//    MockHttpServletResponse response = new MockHttpServletResponse();
//
//    // Act
//    resolver.addCookie(response, "CookieTest");
//
//    // Assert
//    assertThat(response.getCookies()[0].getValue(), is("CookieTest"));
//  }

  @Test
  public void testResolveLocaleWithNoLocale() {
    // Arrange
    AngularCookieLocaleResolver resolver = new AngularCookieLocaleResolver();
    HttpServletRequest servlet = Mockito.mock(HttpServletRequest.class);
    when(servlet.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME)).thenReturn(null);

    // Act
    Locale locale = resolver.resolveLocale(servlet);

    // Assert
    assertThat(locale, is(nullValue()));
  }

  @Test
  public void testResolveLocaleByCookie() {
    // Arrange
    AngularCookieLocaleResolver resolver = new AngularCookieLocaleResolver("locale");
    HttpServletRequest servlet = Mockito.mock(HttpServletRequest.class);
    Cookie cookie = new Cookie("locale", "de");
    Cookie[] cookies = new Cookie[1];
    cookies[0] = cookie;
    when(servlet.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME)).thenReturn(null);
    when(servlet.getCookies()).thenReturn(cookies);

    // Act
    Locale locale = resolver.resolveLocale(servlet);

    // Assert
    assertThat(locale, is(nullValue()));
  }

}
