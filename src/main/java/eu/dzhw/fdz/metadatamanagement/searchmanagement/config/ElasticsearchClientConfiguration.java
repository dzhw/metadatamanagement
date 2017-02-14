package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

/**
 * Spring Configuration which instantiates the Jest Elasticsearch Client.
 * 
 * @author René Reitmann
 */
@Configuration
public class ElasticsearchClientConfiguration {

  @Autowired
  private MetadataManagementProperties metadataManagementProperties;

  /**
   * Create the client which connects to Elasticsearch. Connects to localhost if we are not running
   * in the cloud.
   * 
   * @return The configured {@link JestClient}
   * @throws Exception if the connection params cannot be resolved from the environment in the cloud
   */
  @Bean
  public JestClient jestClient(String elasticSearchConnectionUrl) throws Exception {
    int readTimeout = metadataManagementProperties.getElasticsearchClient()
        .getReadTimeout();

    // configure elasticsearch json serialization / deserialization
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateConverter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
        .create();
    
    // Configuration
    HttpClientConfig clientConfig =
        new HttpClientConfig.Builder(elasticSearchConnectionUrl).readTimeout(readTimeout)
          .multiThreaded(true)
          .gson(gson)
          .build();

    // Construct a new Jest client according to configuration via factory
    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(clientConfig);
    return factory.getObject();
  }

  /**
   * Create the connection url for Elastucsearch client.
   * 
   * @return The connection url
   * @throws Exception if the connection params cannot be resolved from the environment in the cloud
   */
  @Bean
  public String elasticSearchConnectionUrl(Environment environment) throws Exception {
    String connectionUrl = metadataManagementProperties.getElasticsearchClient()
        .getUrl();

    return connectionUrl;
  }
}
