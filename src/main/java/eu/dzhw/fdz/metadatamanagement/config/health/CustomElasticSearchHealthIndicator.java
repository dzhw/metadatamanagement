package eu.dzhw.fdz.metadatamanagement.config.health;

import java.util.List;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.ElasticsearchHealthIndicatorProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

/**
 * Custom implementation of the health indicator of spring boot actuator for elasticsearch. It adds
 * a better mapping of the elasticsearch status (Green, Yellow, Red) and adds the cluster nodes
 * name.
 * 
 * @author René Reitmann
 */
public class CustomElasticSearchHealthIndicator extends AbstractHealthIndicator {

  // constant for the all index
  private static final String[] ALL_INDEX = {"_all"};

  // the elasticsearch client
  private final Client client;

  // some properties
  private final ElasticsearchHealthIndicatorProperties properties;

  public CustomElasticSearchHealthIndicator(final Client client,
      final ElasticsearchHealthIndicatorProperties properties) {
    this.client = client;
    this.properties = properties;
  }

  @Override
  protected void doHealthCheck(Health.Builder builder) throws Exception {
    List<String> indices = this.properties.getIndices();
    ClusterHealthResponse response = this.client.admin().cluster()
        .health(Requests.clusterHealthRequest(
            indices.isEmpty() ? ALL_INDEX : indices.toArray(new String[indices.size()])))
        .actionGet(this.properties.getResponseTimeout());
    switch (response.getStatus()) {
      case GREEN:
        builder.up();
        break;
      case YELLOW:
        builder.status(Status.UNKNOWN);
        break;
      case RED:
      default:
        builder.down();
        break;
    }

    builder.withDetail("nodeName", client.settings().get("name"));
    builder.withDetail("clusterName", response.getClusterName());
    builder.withDetail("numberOfNodes", response.getNumberOfNodes());
    builder.withDetail("numberOfDataNodes", response.getNumberOfDataNodes());
    builder.withDetail("activePrimaryShards", response.getActivePrimaryShards());
    builder.withDetail("activeShards", response.getActiveShards());
    builder.withDetail("relocatingShards", response.getRelocatingShards());
    builder.withDetail("initializingShards", response.getInitializingShards());
    builder.withDetail("unassignedShards", response.getUnassignedShards());
  }

}
