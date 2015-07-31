package eu.dzhw.fdz.metadatamanagement.config.i18n;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * LocaleChangeInterceptor which changes the locale if and only if the requested language is
 * supported.
 * 
 * @author René Reitmann
 */
public class RestrictedLocaleChangeInterceptor extends LocaleChangeInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws ServletException {
    String newLanguage = request.getParameter(super.getParamName());
    for (int i = 0; i < I18nConfiguration.SUPPORTED_LANGUAGES.length; i++) {
      if (I18nConfiguration.SUPPORTED_LANGUAGES[i].getLanguage().equals(newLanguage)) {
        return super.preHandle(request, response, handler);
      }
    }
    // requested language is not supported so we do not change the locale
    return true;
  }
}
