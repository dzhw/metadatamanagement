package eu.dzhw.fdz.metadatamanagement.common.config.locale;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.util.WebUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Angular cookie saved the locale without a double quote (en). So the default
 * CookieLocaleResolver#StringUtils.parseLocaleString(localePart) is not able to parse the locale.
 * This class will check if a double quote has been added, if so it will remove it.
 */
public class AngularCookieLocaleResolver extends CookieLocaleResolver {

  @Override
  public Locale resolveLocale(HttpServletRequest request) {
    parseLocaleCookieIfNecessary(request);
    return (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
  }

  @Override
  @SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC_ANON")
  public LocaleContext resolveLocaleContext(final HttpServletRequest request) {
    parseLocaleCookieIfNecessary(request);
    return new LocaleContext() {
      @Override
      public Locale getLocale() {
        return (Locale) request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
      }
    };
  }

  private void parseLocaleCookieIfNecessary(HttpServletRequest request) {
    if (request.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME) == null) {
      // Retrieve and parse cookie value.
      Cookie cookie = WebUtils.getCookie(request, getCookieName());
      Locale locale = null;
      if (cookie != null) {
        String localePart = cookie.getValue();
                
        locale = (!"-".equals(localePart)
            ? StringUtils.parseLocaleString(localePart.replace('-', '_')) : null);
                
        if (logger.isTraceEnabled()) {
          logger.trace("Parsed cookie value [" + cookie.getValue() + "] into locale '" + locale
              + "'");
        }
      }
      request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME,
          (locale != null ? locale : determineDefaultLocale(request)));
    }
  }
}
