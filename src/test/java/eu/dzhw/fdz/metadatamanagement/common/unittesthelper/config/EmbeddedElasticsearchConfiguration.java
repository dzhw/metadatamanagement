/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.config;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Embedded in-memory elasticsearch node. It will start only once by the
 * {@code UnitTestConfiguration}. The node name is 'UnitTestNode'. The node is by default reachable
 * by the url http://localhost:9234.
 * 
 * @author Daniel Katzberg
 */
@Configuration
public class EmbeddedElasticsearchConfiguration {

  @Bean
  public Node node() {
    // Elasticsearch Settings
    Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
      .put("http.enabled", "true")
      .put("index.number_of_shards", 1)
      .put("index.number_of_replicas", 1)
      .put("node.name", "UnitTestNode")
      .put("http.port", 9234)
      .put("cluster.name","metadatamanagement-test")
      .put("path.home","target/elasticsearch")
      .put("path.logs","target/elasticsearch/logs")
      .put("path.data","target/elasticsearch/data");      

    // Start the elasticsearch in-memory node
    return nodeBuilder().local(true)
      .settings(elasticsearchSettings.build())
      .node();
  }
}
