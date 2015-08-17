package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;

/**
 * Custom {@link LocaleResolver} which extracts the language from the requested url (e.g. *
 * "/de/variables" )and checks if it is a supported language. If the url does not contain a
 * supported language the accept header is checked for a supported locale. If neither the url nor
 * the accept-header contains a supported language {@link I18nConfiguration.DEFAULT_LOCALE} will be
 * used.
 * 
 * @author René Reitmann
 */
public class PathLocaleResolver implements LocaleResolver {

  public static final String LOCALE_ATTRIBUTE = PathLocaleResolver.class + ".LOCALE";

  @Override
  public Locale resolveLocale(HttpServletRequest servletRequest) {
    parsePathIfNecessary(servletRequest);
    return (Locale) servletRequest.getAttribute(LOCALE_ATTRIBUTE);
  }

  /**
   * Cache the resolved locale as request attribute since the resolver is called multiple times per
   * request.
   * 
   * @param servletRequest the complete servlet request
   */
  private void parsePathIfNecessary(HttpServletRequest servletRequest) {
    if (servletRequest.getAttribute(LOCALE_ATTRIBUTE) == null) {
      String url =
          servletRequest.getRequestURI().substring(servletRequest.getContextPath().length());
      Optional<Locale> supportedLocale = I18nConfiguration.SUPPORTED_LANGUAGES.stream()
          .filter(locale -> url.startsWith("/" + locale.getLanguage())).findAny();
      if (supportedLocale.isPresent()) {
        servletRequest.setAttribute(LOCALE_ATTRIBUTE, supportedLocale.get());
        return;
      }

      // get all locales from the clients accept header
      Enumeration<Locale> acceptedLocales = servletRequest.getLocales();

      while (acceptedLocales.hasMoreElements()) {
        Locale acceptedLocale = acceptedLocales.nextElement();
        if (I18nConfiguration.SUPPORTED_LANGUAGES.contains(acceptedLocale)) {
          // return the accept-header locale if supported
          servletRequest.setAttribute(LOCALE_ATTRIBUTE, acceptedLocale);
          return;
        }
      }
      // use english as the default locale
      servletRequest.setAttribute(LOCALE_ATTRIBUTE, I18nConfiguration.DEFAULT_LOCALE);
    }
  }

  @Override
  public void setLocale(final HttpServletRequest servletRequest,
      final HttpServletResponse servletResponse, final Locale locale) {
    throw new UnsupportedOperationException();
  }
}
