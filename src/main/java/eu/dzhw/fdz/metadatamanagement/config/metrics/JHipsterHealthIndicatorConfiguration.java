package eu.dzhw.fdz.metadatamanagement.config.metrics;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The jhipster health indicator config.
 *
 */
@Configuration
public class JHipsterHealthIndicatorConfiguration {

  @Inject
  private DataSource dataSource;

  @Bean
  public HealthIndicator dbHealthIndicator() {
    return new DatabaseHealthIndicator(dataSource);
  }
}
