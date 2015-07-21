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
 * 
 */

/**
 * @author reitmann
 *
 */
public class CustomElasticSearchHealthIndicator extends AbstractHealthIndicator {
    private static final String[] allIndices = { "_all" };
    private Client client;
    private ElasticsearchHealthIndicatorProperties properties;

    public CustomElasticSearchHealthIndicator(Client client,
	    ElasticsearchHealthIndicatorProperties properties) {
	this.client = client;
	this.properties = properties;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
	List<String> indices = this.properties.getIndices();
	ClusterHealthResponse response = this.client
		.admin()
		.cluster()
		.health(Requests.clusterHealthRequest(indices.isEmpty() ? allIndices
			: indices.toArray(new String[indices.size()])))
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
	builder.withDetail("activePrimaryShards",
		response.getActivePrimaryShards());
	builder.withDetail("activeShards", response.getActiveShards());
	builder.withDetail("relocatingShards", response.getRelocatingShards());
	builder.withDetail("initializingShards",
		response.getInitializingShards());
	builder.withDetail("unassignedShards", response.getUnassignedShards());
    }

}
