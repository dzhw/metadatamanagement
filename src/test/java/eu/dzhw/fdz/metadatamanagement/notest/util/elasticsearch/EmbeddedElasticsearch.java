/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.notest.util.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;

/**
 * Embedded in-memory elasticsearch node. It will start only once by the
 * {@code UnitTestConfiguration}. The node name is 'UnitTestNode'. The node is by default reachable
 * by the url http://localhost:9234.
 * 
 * @author Daniel Katzberg
 *
 */
public class EmbeddedElasticsearch {

  /** The node of elasticsearch */
  private Node node;

  /**
   * Private constructor of the singleton embedded server.
   */
  public EmbeddedElasticsearch() {
    // Elasticsearch Settings
    ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder()
      .put("http.enabled", "true")
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 1)
      .put("node.name", "UnitTestNode")
      .put("http.port", 9234);

    // Start the elasticsearch in-memory node
    this.node = nodeBuilder().local(false)
      .settings(elasticsearchSettings.build())
      .node();
  }

  /* GETTER / SETTER */
  public Node getNode() {
    return node;
  }

}
