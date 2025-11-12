package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.health_report.IndicatorHealthStatus;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * HealthIndicator for the Elasticsearch service.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchHealthIndicator extends AbstractHealthIndicator {

  private final ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  private final ElasticsearchClient elasticsearchClient;

  @Override
  protected void doHealthCheck(Builder builder) {

    IndicatorHealthStatus status;
    try {
      status = this.elasticsearchClient.healthReport().status();
    } catch (IOException e) {
      builder.down();
      builder.withDetail("IOException", e.getMessage());
      return;
    }

    if (status == IndicatorHealthStatus.Green || status == IndicatorHealthStatus.Yellow) {
      builder.up();
    } else {
      builder.down();
    }

    builder.withDetail("Number of Update Queue Items", elasticsearchUpdateQueueItemRepository.count());
  }
}
