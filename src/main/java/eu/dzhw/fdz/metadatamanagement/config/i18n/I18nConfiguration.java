package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring configuration which adds i18n based on cookies.
 * 
 * @author René Reitmann
 */
@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

  public static final Set<Locale> SUPPORTED_LANGUAGES;
  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  static {
    SUPPORTED_LANGUAGES = new HashSet<>(2);
    SUPPORTED_LANGUAGES.add(Locale.ENGLISH);
    SUPPORTED_LANGUAGES.add(Locale.GERMAN);
  }

  public I18nConfiguration() {
    // set the system locale to english
    Locale.setDefault(DEFAULT_LOCALE);
  }

  /**
   * Add a cookie locale resolver which determines the users current language by looking into a
   * cookie if present. If not present it will use german as default.
   * 
   * @return the cookie locale resolver
   */
  @Bean
  public LocaleResolver localeResolver() {
    return new PathLocaleResolver();
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
