package eu.dzhw.fdz.metadatamanagement.config.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

/**
 * Custom Elasticsearch Configuration.
 * 
 * @author René Reitmann
 */
@Configuration
public class ElasticsearchConfiguration {

  /**
   * Create a custom ElasticsearchTemplate with a json mapper which knows to map JSR 310 fields
   * (e.g. LocalDate).
   * 
   * @param client A refernce to the elasticsearch client-
   * @return a customized ElasticsearchTemplate
   */
  @Bean
  public ElasticsearchTemplate elasticsearchTemplate(Client client) {
    JacksonDocumentMapper entityMapper = new JacksonDocumentMapper();
    return new ElasticsearchTemplate(client, entityMapper);
  }
}
