package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import eu.dzhw.fdz.metadatamanagement.common.config.locale.AngularCookieLocaleResolver;

/**
 * Configure the {@link MessageSource} and the {@link LocaleResolver}.
 */
@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

  @Autowired
  private Environment environment;

  /**
   * The {@link LocaleResolver}.
   */
  @Bean(name = "localeResolver")
  public LocaleResolver localeResolver() {
    AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
    cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
    return cookieLocaleResolver;
  }

  /**
   * The {@link MessageSource}.
   */
  @Bean
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(environment.getProperty(
        "spring.messages.cache-seconds", Integer.class, -1));

    return messageSource;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("language");
    registry.addInterceptor(localeChangeInterceptor);
  }
}
