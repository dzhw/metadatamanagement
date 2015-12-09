/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.unittest.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.dzhw.fdz.metadatamanagement.unittest.util.elasticsearch.EmbeddedElasticsearch;

/**
 * @author Daniel Katzberg
 *
 */
@Configuration
public class UnitTestConfiguration {

  @Bean
  public EmbeddedElasticsearch embeddedElasticsearch() {
    return new EmbeddedElasticsearch();
  }
}
