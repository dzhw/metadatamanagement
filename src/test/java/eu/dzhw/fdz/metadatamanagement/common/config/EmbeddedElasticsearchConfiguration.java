/**
 *
 */
package eu.dzhw.fdz.metadatamanagement.common.config;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.JavaHomeOption;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

/**
 * Embedded in-memory elasticsearch node. It will start only once by the
 * {@code UnitTestConfiguration}. The node name is 'UnitTestNode'. The node is by default reachable
 * by the url http://localhost:19234.
 *
 * @author Daniel Katzberg
 */
@Configuration
@Slf4j
public class EmbeddedElasticsearchConfiguration {

  private static EmbeddedElastic elastic;

//  static {
//    try {
//      elastic = EmbeddedElastic.builder()
//          .withElasticVersion(MetadataManagementProperties.Elasticsearch.TEST_VERSION)
//          .withJavaHome(JavaHomeOption.inheritTestSuite()).withEsJavaOpts("-Xms128m -Xmx1024m")
//          .withSetting(PopularProperties.HTTP_PORT, 19234)
//          .withSetting(PopularProperties.CLUSTER_NAME, "metadatamanagement-test")
//          .withInstallationDirectory(new File("target/elasticsearch"))
//          .withCleanInstallationDirectoryOnStop(false).withStartTimeout(2, TimeUnit.MINUTES).build()
//          .start();
//    } catch (Exception e) {
//      log.error("Unable to start embedded elasticsearch", e);
//    }
//  }
}
