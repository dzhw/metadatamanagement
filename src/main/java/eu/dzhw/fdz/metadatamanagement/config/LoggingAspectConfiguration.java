package eu.dzhw.fdz.metadatamanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import eu.dzhw.fdz.metadatamanagement.aop.logging.LoggingAspect;
/**
 * Configuration for the logging aspects.
 *
 */
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspectConfiguration {

  @Bean
  @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
  public LoggingAspect loggingAspect() {
    return new LoggingAspect();
  }
}
