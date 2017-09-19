package eu.dzhw.fdz.metadatamanagement.searchmanagement.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;

import eu.dzhw.fdz.metadatamanagement.searchmanagement.repository.ElasticsearchUpdateQueueItemRepository;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Stats;

/**
 * HealthIndicator for Spring Boot Actuator which uses the {@link JestClient} for indicating health.
 * 
 * @author René Reitmann
 */
@Component
public class ElasticsearchHealthIndicator extends AbstractHealthIndicator {

  private final JestClient jestClient;
  
  private final ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository;

  @Autowired
  public ElasticsearchHealthIndicator(JestClient jestClient,
      ElasticsearchUpdateQueueItemRepository elasticsearchUpdateQueueItemRepository) {
    this.jestClient = jestClient;
    this.elasticsearchUpdateQueueItemRepository = elasticsearchUpdateQueueItemRepository;
  }

  @Override
  protected void doHealthCheck(Builder builder) throws Exception {
    JestResult result = jestClient.execute(new Stats.Builder().addIndex("_all").build());
    JsonObject map = result.getJsonObject();

    if (result.isSucceeded()) {
      builder.up();
      fillStatsForIndex(map, builder);
    } else {
      builder.down();
    }
    builder.withDetail("Number of Update Queue Items", 
        elasticsearchUpdateQueueItemRepository.count());
  }

  /**
   * Fill statistics for all indices.
   * 
   * @param map The json object for the _all index.
   * @param builder The spring boot builder.
   */
  private void fillStatsForIndex(JsonObject map, Builder builder) {
    JsonObject indices = map.getAsJsonObject("indices");
    indices.entrySet().forEach(entry -> {
      String indexName = entry.getKey();
      JsonObject indexStats = entry.getValue().getAsJsonObject();
      JsonObject primaries = indexStats.getAsJsonObject("primaries");
      builder.withDetail(indexName + "." + "docs_count",
          primaries.getAsJsonObject("docs").get("count").getAsString());
    });
  }
}
