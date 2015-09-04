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
   * Create a jackson mapper for communication with elasticsearch.
   * 
   * @return a jackson mapper for communication with elasticsearch
   */
  @Bean
  public JacksonDocumentMapper jacksonDocumentMapper() {
    return new JacksonDocumentMapper();
  }

  /**
   * Create a custom ElasticsearchTemplate with a json mapper which knows to map JSR 310 fields
   * (e.g. LocalDate).
   * 
   * @param jacksonDocumentMapper the {@link JacksonDocumentMapper}
   * @param client A refernce to the elasticsearch client-
   * @return a customized ElasticsearchTemplate
   */
  @Bean
  public ElasticsearchTemplate elasticsearchTemplate(Client client,
      JacksonDocumentMapper jacksonDocumentMapper) {
    return new ElasticsearchTemplate(client, jacksonDocumentMapper);
  }

  /**
   * Create a custom mapper which is capable of deserializing aggregations.
   * 
   * @param jacksonDocumentMapper the {@link JacksonDocumentMapper}
   * @return a custom mapper which is capable of deserializing aggregations.
   */
  @Bean
  public AggregationResultMapper aggregationResultMapper(
      JacksonDocumentMapper jacksonDocumentMapper) {
    return new AggregationResultMapper(jacksonDocumentMapper);
  }
}
