package eu.dzhw.fdz.metadatamanagement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.searchbox.client.JestClient;

/**
 * Configuration Properties which are defined by our application.
 * 
 * <p>
 * Properties are configured in the application-{profile}.yml file.
 * </p>
 * 
 * @author René Reitmann
 */
@ConfigurationProperties(prefix = "metadatamanagement", ignoreUnknownFields = false)
public class MetadataManagementProperties {

  private final ElasticsearchClient elasticsearchClient = new ElasticsearchClient();

  public ElasticsearchClient getElasticsearchClient() {
    return elasticsearchClient;
  }

  /**
   * Configuration Properties for the {@link JestClient}.
   * 
   * @author René Reitmann
   */
  public static class ElasticsearchClient {
    // default connection url
    private String url = "http://localhost:9200";

    public String getUrl() {
      return this.url;
    }

    public void setUrl(String url) {
      this.url = url;
    }
  }
}
