package eu.dzhw.fdz.metadatamanagement.searchmanagement.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
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

  @Inject
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
    int readTimeout = metadataManagementProperties.getElasticsearchClient().getReadTimeout();

    // Configuration
    HttpClientConfig clientConfig = new HttpClientConfig.Builder(elasticSearchConnectionUrl)
        .readTimeout(readTimeout).multiThreaded(true).build();

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

  @SuppressWarnings("rawtypes")
  @Bean
  public String elasticSearchConnectionUrl(Environment environment) throws Exception {
    String connectionUrl = metadataManagementProperties.getElasticsearchClient().getUrl();

    // use cloud connection url if available
    if (environment.acceptsProfiles(Constants.SPRING_PROFILE_CLOUD)) {
      // Using jackson to parse VCAP_SERVICES
      HashMap result = new ObjectMapper().readValue(System.getenv("VCAP_SERVICES"), HashMap.class);

      connectionUrl =
          (String) ((Map) ((Map) ((List) result.get("searchly")).get(0)).get("credentials"))
              .get("uri");
    }
    return connectionUrl;
  }
}
