package eu.dzhw.fdz.metadatamanagement.config.metrics;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.searchbox.client.JestClient;

/**
 * Create additional {@link HealthIndicator} for spring boot actuator.
 * 
 * @author René Reitmann
 */
@Configuration
public class JHipsterHealthIndicatorConfiguration {
  /**
   * Add custom {@link ElasticsearchHealthIndicator} to the application context.
   * @param jestClient The client for pinging elasticsearch
   * @return {@link ElasticsearchHealthIndicator}
   * @author René Reitmann
   */
  @Bean()
  public HealthIndicator elasticsearchHealthIndicator(JestClient jestClient) {
    return new ElasticsearchHealthIndicator(jestClient);
  }
}
