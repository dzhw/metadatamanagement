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
  
  private final ElasticsearchAngularClient elasticsearchAngularClient = 
      new ElasticsearchAngularClient();

  public ElasticsearchClient getElasticsearchClient() {
    return elasticsearchClient;
  }
  
  public ElasticsearchAngularClient getElasticsearchAngularClient() {
    return elasticsearchAngularClient;
  }

  /**
   * Configuration Properties for the {@link JestClient}.
   * 
   * @author René Reitmann
   */
  public static class ElasticsearchClient {
    // default connection url
    private String url = "http://localhost:9200";
    private int readTimeout = 60000;

    public String getUrl() {
      return this.url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public int getReadTimeout() {
      return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
      this.readTimeout = readTimeout;
    }
  }
  
  /**
   * Configuration Properties for the Angular ElasticSearch Client.
   * 
   * @author Amine Limouri
   */
  public static class ElasticsearchAngularClient {
    // default connection url
    private String url = "http://localhost:9200";
    private String versionApi = "1.7";
    private String logLevel = "trace";
        

    public String getUrl() {
      return this.url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getVersionApi() {
      return versionApi;
    }

    public void setVersionApi(String versionApi) {
      this.versionApi = versionApi;
    }

    public String getLogLevel() {
      return logLevel;
    }

    public void setLogLevel(String logLevel) {
      this.logLevel = logLevel;
    }
  }
}
