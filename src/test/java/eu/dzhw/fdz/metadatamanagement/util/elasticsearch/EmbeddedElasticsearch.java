/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.util.elasticsearch;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;

/**
 * SINGLETON
 * 
 * @author Daniel Katzberg
 *
 */
public class EmbeddedElasticsearch {

  /** SINGLETON of the embeddedElasticsearch */
  private static volatile EmbeddedElasticsearch embeddedElasticsearch;

  /** The local node of elasticsearch */
  private Node node;

  /**
   * PRIVATE CONSTRUCTOR.
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
   * @return The embedded elasticsearch server is a singleton
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
