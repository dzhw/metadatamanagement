package eu.dzhw.fdz.metadatamanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.aop.logging.LoggingAspect;

/**
 * Configure aop proxies.
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  /**
   * Register the {@link LoggingAspect} when running in dev mode.
   */
  @Bean
  @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
