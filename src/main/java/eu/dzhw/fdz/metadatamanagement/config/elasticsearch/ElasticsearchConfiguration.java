package eu.dzhw.fdz.metadatamanagement.config.elasticsearch;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import eu.dzhw.fdz.metadatamanagement.data.questionmanagement.aggregations.QuestionDocumentAggregationResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.aggregations.VariableDocumentAggregrationResultMapper;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;

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
  public VariableDocumentAggregrationResultMapper variableDocumentAggregationResultMapper(
      JacksonDocumentMapper jacksonDocumentMapper, ScaleLevelProvider scaleLevelProvider) {
    return new VariableDocumentAggregrationResultMapper(jacksonDocumentMapper, scaleLevelProvider);
  }

  /**
   * Create a custom mapper for deserialization of aggregations.
   * 
   * @param jacksonDocumentMapper the {@link JacksonDocumentMapper}
   * @return a custom mapper which is capable of deserializing aggregations.
   */
  @Bean
  public QuestionDocumentAggregationResultMapper questionDocumentAggregationResultMapper(
      JacksonDocumentMapper jacksonDocumentMapper) {
    return new QuestionDocumentAggregationResultMapper(jacksonDocumentMapper);
  }
}
