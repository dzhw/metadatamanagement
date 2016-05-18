package eu.dzhw.fdz.metadatamanagement.logmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.logmanagement.aop.LoggingAspect;

/**
 * Configure aop proxies.
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  /**
   * Register the {@link LoggingAspect} when running in local mode.
   */
  @Bean
  @Profile(Constants.SPRING_PROFILE_LOCAL)
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
