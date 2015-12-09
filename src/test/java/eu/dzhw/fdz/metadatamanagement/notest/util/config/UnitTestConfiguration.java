/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.notest.util.config;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import eu.dzhw.fdz.metadatamanagement.notest.util.elasticsearch.EmbeddedElasticsearch;

/**
 * @author Daniel Katzberg
 *
 */
@Configuration
public class UnitTestConfiguration {
  
  private EmbeddedElasticsearch embeddedElasticsearch;
    
  @Singleton
  @Bean
  public EmbeddedElasticsearch embeddedElasticsearch() {
    if(this.embeddedElasticsearch == null) {
      this.embeddedElasticsearch = new EmbeddedElasticsearch();
    }
    
    return this.embeddedElasticsearch;
  }
  
}
