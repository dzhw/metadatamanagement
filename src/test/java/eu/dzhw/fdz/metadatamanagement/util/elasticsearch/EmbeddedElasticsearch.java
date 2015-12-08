/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.util.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;

/**
 * Embedded elasticsearch server. It will start only once by the BasicTest.before method. The node
 * name is 'UnitTestNode'. The node is by default reachable by the url http://localhost:9200.
 * 
 * @author Daniel Katzberg
 *
 */
public class EmbeddedElasticsearch {

  /** Singleton of the embeddedElasticsearch */
  private static volatile EmbeddedElasticsearch embeddedElasticsearch;

  /** The node of elasticsearch */
  private Node node;

  /**
   * Private constructor of the singleton embedded server.
   */
  private EmbeddedElasticsearch() {
    // Elasticsearch Settings
    ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder()
      .put("http.enabled", "true")
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 1)
      .put("node.name", "UnitTestNode");

    // Start the elasticsearch node
    this.node = nodeBuilder().local(false)
      .settings(elasticsearchSettings.build())
      .node();
  }

  /**
   * Shutdown the singleton elasticsearch node
   */
  public void shutdown() {
    // Close the elasticsearch node.
    if (this.node != null) {
      this.node.close();
    }
  }

  /**
   * @return The embedded elasticsearch server is a singleton. This method will return the only
   *         instance of the singleton.
   */
  public static EmbeddedElasticsearch getEmbeddedElasticsearch() {
    if (embeddedElasticsearch == null) {
      synchronized (EmbeddedElasticsearch.class) {
        if (embeddedElasticsearch == null) {
          embeddedElasticsearch = new EmbeddedElasticsearch();
        }
      }
    }

    return embeddedElasticsearch;
  }

  /* GETTER / SETTER */
  public Node getNode() {
    return node;
  }

}
