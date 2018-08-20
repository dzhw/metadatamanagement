/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.unittesthelper.config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

/**
 * Embedded in-memory elasticsearch node. It will start only once by the
 * {@code UnitTestConfiguration}. The node name is 'UnitTestNode'. The node is by default reachable
 * by the url http://localhost:9234.
 *
 * @author Daniel Katzberg
 */
@Configuration
public class EmbeddedElasticsearchConfiguration {

  @Autowired
  private MetadataManagementProperties properties;
  
  @Bean
  public EmbeddedElastic embeddedElastic() throws IOException, InterruptedException {
    final EmbeddedElastic embeddedElastic = EmbeddedElastic.builder()
        .withElasticVersion(properties.getElasticsearch().getVersion())
        .withEsJavaOpts("-Xms128m -Xmx512m")
        .withSetting(PopularProperties.HTTP_PORT, 9234)
        .withSetting(PopularProperties.CLUSTER_NAME, "metadatamanagement-test")
        .withInstallationDirectory(new File("target/elasticsearch"))
        .withStartTimeout(2, TimeUnit.MINUTES)
        .build()
        .start();
        
    return embeddedElastic;
  }
}
