package eu.dzhw.fdz.metadatamanagement.searchmanagement.service;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HealthIndicator for Spring Boot Actuator which uses the {@link JestClient} for indicating health.
 * 
 * @author René Reitmann
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchHealthIndicator extends AbstractHealthIndicator {

  private final RestHighLevelClient client;

  private final ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    try {
      ClusterHealthRequest request = new ClusterHealthRequest();
      ClusterHealthResponse response = client.cluster().health(request, RequestOptions.DEFAULT);

      if (response.getStatus().equals(ClusterHealthStatus.GREEN)
          || response.getStatus().equals(ClusterHealthStatus.YELLOW)) {
        builder.up();
      } else {
        builder.down();
      }
      builder.withDetail("Number of Update Queue Items",
          elasticsearchUpdateQueueItemRepository.count());
    } catch (Exception e) {
      log.error("Elasticsearch health check failed:", e);
      builder.down(e);
    }
  }
}
