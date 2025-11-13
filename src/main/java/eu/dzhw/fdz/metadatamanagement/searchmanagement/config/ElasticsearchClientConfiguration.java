package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration which instantiates the Elasticsearch REST Client.
 */
@Configuration
@Slf4j
public class ElasticsearchClientConfiguration {

  @Value("${spring.elasticsearch.rest.uris[0]}")
  private String url;

  @Bean(destroyMethod = "close")
  public ElasticsearchClient elasticsearchClient() {
    final var restClient = RestClient.builder(HttpHost.create(this.url)).build();
    final var jsonMapper = new JacksonJsonpMapper(new ObjectMapper().registerModule(new JavaTimeModule()));
    final var transport = new RestClientTransport(restClient, jsonMapper);
    return new ElasticsearchClient(transport);
  }
}
