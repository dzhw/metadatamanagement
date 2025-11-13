package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;

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

    // extract basic auth credentials from URL if necessary and prepare auth header
    final var headers = new ArrayList<Header>();
    final var chunks = this.url.split("@");
    var host = chunks.length == 2 ? chunks[1] : url;
    if (chunks.length == 2) {
      host = this.url.startsWith("https") ? "https://" + host : "http://" + host;
      final var credentials = chunks[0]
        .replace("http://", "")
        .replace("https://", "");
      final var encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
      headers.add(new BasicHeader("Authorization", "Basic " + encoded));
    }

    final var restClient = RestClient.builder(HttpHost.create(host))
      .setDefaultHeaders(headers.toArray(new Header[]{}))
      .build();
    final var jsonMapper = new JacksonJsonpMapper(new ObjectMapper().registerModule(new JavaTimeModule()));
    final var transport = new RestClientTransport(restClient, jsonMapper);
    return new ElasticsearchClient(transport);
  }
}
