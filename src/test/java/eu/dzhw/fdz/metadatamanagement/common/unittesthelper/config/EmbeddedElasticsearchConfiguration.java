/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.config;

import java.util.Arrays;
import java.util.Collection;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;
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

  @SuppressWarnings("resource")
  @Bean
  public Node node() throws NodeValidationException {
    // Elasticsearch Settings
    
    Settings elasticsearchSettings = Settings.builder()
      .put("http.enabled", "true")
      .put("node.name", "UnitTestNode")
      .put("http.port", 9234)
      .put("http.type", "netty4")
      .put("cluster.name","metadatamanagement-test")
      .put("path.home","target/elasticsearch")
      .put("path.logs","target/elasticsearch/logs")
      .put("path.data","target/elasticsearch/data")
      .put("transport.type","local").build();

    // Start the elasticsearch in-memory node
    Node node = new PluginConfigurableNode(elasticsearchSettings, Arrays.asList(Netty4Plugin.class)).start();
    return node;
  }
  
  private static class PluginConfigurableNode extends Node {
    public PluginConfigurableNode(Settings settings, Collection<Class<? extends Plugin>> classpathPlugins) {
        super(InternalSettingsPreparer.prepareEnvironment(settings, null), classpathPlugins);
    }
}
}
