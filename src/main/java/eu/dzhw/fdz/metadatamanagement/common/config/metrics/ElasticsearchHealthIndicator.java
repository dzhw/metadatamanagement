package eu.dzhw.fdz.metadatamanagement.common.config.metrics;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

import com.google.gson.JsonObject;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Stats;

/**
 * HealthIndicator for Spring Boot Actuator which uses the {@link JestClient} for indicating health.
 * 
 * @author René Reitmann
 */
public class ElasticsearchHealthIndicator extends AbstractHealthIndicator {

  private final JestClient jestClient;

  public ElasticsearchHealthIndicator(JestClient jestClient) {
    this.jestClient = jestClient;
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
      builder.withDetail(indexName + "." + "docs_deleted",
          primaries.getAsJsonObject("docs").get("deleted").getAsString());
      builder.withDetail(indexName + "." + "store_size",
          primaries.getAsJsonObject("store").get("size_in_bytes").getAsString());
      builder.withDetail(indexName + "." + "query_total",
          primaries.getAsJsonObject("search").get("query_total").getAsString());
    });
  }
}
