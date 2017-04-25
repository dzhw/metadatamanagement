package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
  
  private final Dara dara = new Dara();

  public ElasticsearchClient getElasticsearchClient() {
    return elasticsearchClient;
  }

  public ElasticsearchAngularClient getElasticsearchAngularClient() {
    return elasticsearchAngularClient;
  }
  
  public Dara getDara() {
    return dara;
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
    private String apiVersion = "5.x";
    private String logLevel = "trace";
    private Integer pageSize = 10;

    public String getUrl() {
      return this.url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getApiVersion() {
      return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
    }

    public String getLogLevel() {
      return logLevel;
    }

    public void setLogLevel(String logLevel) {
      this.logLevel = logLevel;
    }

    public Integer getPageSize() {
      return pageSize;
    }

    public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
    }
  }
  
  /**
   * Configuration Properties for Dara.
   *
   * @author Daniel Katzberg
   */
  public static class Dara {
    private String endpoint;
    private String username;
    private String password;

    public String getEndpoint() {
      return endpoint;
    }

    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }

    @SuppressFBWarnings("NM_CONFUSING")
    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }
}
