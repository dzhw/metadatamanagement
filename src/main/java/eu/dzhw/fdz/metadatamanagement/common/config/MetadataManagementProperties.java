package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.searchbox.client.JestClient;

/**
 * Configuration Properties which are defined by our application.
 * Properties are configured in the application-{profile}.yml file.
 *
 * @author René Reitmann
 */
@ConfigurationProperties(prefix = "metadatamanagement", ignoreUnknownFields = false)
public class MetadataManagementProperties {

  private final Elasticsearch elasticsearch = new Elasticsearch();

  private final ElasticsearchClient elasticsearchClient = new ElasticsearchClient();

  private final ElasticsearchAngularClient elasticsearchAngularClient =
      new ElasticsearchAngularClient();

  private final Dara dara = new Dara();

  private final Dlp dlp = new Dlp();

  private final Rabbitmq rabbitmq = new Rabbitmq();

  private final Websockets websockets = new Websockets();

  private final Server server = new Server();

  private final Ordermanagement ordermanagement = new Ordermanagement();

  private final ProjectManagement projectManagement = new ProjectManagement();

  public Elasticsearch getElasticsearch() {
    return elasticsearch;
  }

  public ElasticsearchClient getElasticsearchClient() {
    return elasticsearchClient;
  }

  public ElasticsearchAngularClient getElasticsearchAngularClient() {
    return elasticsearchAngularClient;
  }

  public Dara getDara() {
    return dara;
  }

  public Dlp getDlp() {
    return dlp;
  }

  public Rabbitmq getRabbitmq() {
    return rabbitmq;
  }

  public Websockets getWebsockets() {
    return websockets;
  }

  public Server getServer() {
    return server;
  }

  public Ordermanagement getOrdermanagement() {
    return ordermanagement;
  }

  public ProjectManagement getProjectManagement() {
    return projectManagement;
  }

  /**
   * Configure the current elasticsearch server version for testing.
   *
   * @author René Reitmann
   */
  public static class Elasticsearch {
    private String version = "6.3.2";

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

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
    private String apiVersion = "5.1";
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

  /**
   * Configuration Properties for RabbitMQ (ignored in LOCAL mode).
   */
  public static class Rabbitmq {
    private String uri;
    private String username;
    private String password;
    private String host;
    private String virtualHost;

    public String getUri() {
      return uri;
    }

    /**
     * Set the uri and decode its components.
     * @param uri connection uri of the rabbitmq service
     */
    public void setUri(String uri) {
      this.uri = uri;
      UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
      this.host = uriComponents.getHost();
      this.virtualHost = Optional.of(uriComponents.getPath()).orElse("").replaceFirst("/", "");
      this.username = Optional.of(uriComponents.getUserInfo()).orElse("").split(":")[0];
      this.password = Optional.of(uriComponents.getUserInfo()).orElse("").split(":")[1];
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }

    public String getHost() {
      return host;
    }

    public String getVirtualHost() {
      return virtualHost;
    }
  }

  /**
   * Allowed origins for web socket connections.
   *
   * @author René Reitmann
   */
  public static class Websockets {
    private List<String> allowedOrigins = new ArrayList<String>();

    public List<String> getAllowedOrigins() {
      return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
      this.allowedOrigins = allowedOrigins;
    }
  }

  /**
   * Custom Server properties.
   *
   * @author René Reitmann
   */
  public static class Server {
    private String contextRoot = null;

    private Integer instanceIndex = null;

    public Integer getInstanceIndex() {
      return instanceIndex;
    }

    public void setInstanceIndex(Integer instanceIndex) {
      this.instanceIndex = instanceIndex;
    }

    public String getContextRoot() {
      return contextRoot;
    }

    public void setContextRoot(String contextRoot) {
      this.contextRoot = contextRoot;
    }
  }

  /**
   * Custom Order Management properties.
   *
   * @author René Reitmann
   */
  public static class Ordermanagement {
    private String email = "";

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  /**
   * Project Management properties.
   */
  public static class ProjectManagement {
    private String email = "";

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }
  }

  /**
   * DLP properties.
   */
  public static class Dlp {
    private String endpoint;

    public String getEndpoint() {
      return endpoint;
    }

    public void setEndpoint(String endpoint) {
      this.endpoint = endpoint;
    }
  }
}
