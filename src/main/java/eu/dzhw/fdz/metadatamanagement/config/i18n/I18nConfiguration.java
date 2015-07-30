package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import eu.dzhw.fdz.metadatamanagement.MetaDataManagementApplication;

/**
 * Spring configuration which adds i18n based on cookies.
 * 
 * @author René Reitmann
 */
@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

  public static final Locale[] SUPPORTED_LANGUAGES = {Locale.ENGLISH, Locale.GERMAN};

  public I18nConfiguration() {
    // set the system locale to english
    Locale.setDefault(Locale.ENGLISH);
  }

  /**
   * Add a cookie locale resolver which determines the users current language by looking into a
   * cookie if present. If not present it will use german as default.
   * 
   * @return the cookie locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setCookieName(MetaDataManagementApplication.class.getName());
    cookieLocaleResolver.setDefaultLocale(Locale.GERMAN);
    return cookieLocaleResolver;
  }

  /**
   * Intercept get requests with url param "language=de" for instance to change the cookie.
   * 
   * @return the interceptor
   */
  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new RestrictedLocaleChangeInterceptor();
    lci.setParamName("language");
    return lci;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }

  /**
   * Enable automatic binding from ISO formatted strings to JSR 310 compatible date time objects.
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    DateTimeFormatterRegistrar dateTimeFormatterRegistrar = new DateTimeFormatterRegistrar();
    dateTimeFormatterRegistrar.setUseIsoFormat(true);
    dateTimeFormatterRegistrar.registerFormatters(registry);
  }
}
