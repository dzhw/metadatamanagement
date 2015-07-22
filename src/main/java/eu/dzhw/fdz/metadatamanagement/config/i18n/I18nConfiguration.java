package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplication;

@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

  /**
   * Add a cookie locale resolver which determines the users current language by looking into a
   * cookie if present. If not present it will determine the current locale from the accept header.
   * 
   * @return the cookie locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    // set the system locale to en_US
    // this has two effects:
    // 1. all log messages are english
    // 2. english is the fallback locale for the http requests (if neither
    // an accept-header nor a cookie is present)
    Locale.setDefault(Locale.US);
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setCookieName(MetaDataManagementApplication.class.getName());
    return cookieLocaleResolver;
  }

  /**
   * Intercept get requests with url param "locale=de_DE" for instance to change the cookie.
   * 
   * @return the interceptor
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("language");
    return lci;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }
}
