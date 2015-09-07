package eu.dzhw.fdz.metadatamanagement.config.i18n;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.boot.autoconfigure.web.HttpEncodingProperties;
import org.springframework.boot.context.web.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring configuration which adds i18n based on cookies.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

  public static final Set<Locale> SUPPORTED_LANGUAGES;
  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  static {
    Set<Locale> temp = new HashSet<>(2);
    temp.add(Locale.ENGLISH);
    temp.add(Locale.GERMAN);
    SUPPORTED_LANGUAGES = Collections.unmodifiableSet(temp);
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
   * Create the {@link CustomLocalDateFormatter}.
   * 
   * @return the {@link CustomLocalDateFormatter}
   */
  @Bean
  public CustomLocalDateFormatter customLocalDateFormatter() {
    return new CustomLocalDateFormatter();
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

  /**
   * Workaround for https://github.com/spring-projects/spring-boot/issues/3912 .
   * 
   * @param httpEncodingProperties configured {@link HttpEncodingProperties}
   * @return An {@link OrderedCharacterEncodingFilter}
   */
  @Bean
  public OrderedCharacterEncodingFilter characterEncodingFilter(
      HttpEncodingProperties httpEncodingProperties) {
    OrderedCharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
    filter.setEncoding(httpEncodingProperties.getCharset().name());
    filter.setForceEncoding(httpEncodingProperties.isForce());
    filter.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return filter;
  }
}
