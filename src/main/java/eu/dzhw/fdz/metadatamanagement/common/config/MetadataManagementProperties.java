package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuration Properties which are defined by our application.
 * Properties are configured in the application-{profile}.yml file.
 *
 * @author René Reitmann
 */
@ConfigurationProperties(prefix = "metadatamanagement", ignoreUnknownFields = false)
@Getter
@Setter
public class MetadataManagementProperties {

  private final Elasticsearch elasticsearch = new Elasticsearch();

  private final ElasticsearchAngularClient elasticsearchAngularClient =
      new ElasticsearchAngularClient();

  private final Dara dara = new Dara();

  private final DaraPid daraPid = new DaraPid();

  private final DataCite dataCite = new DataCite();

  private final Dlp dlp = new Dlp();

  private final Mongodb mongodb = new Mongodb();

  private final Server server = new Server();

  private final Ordermanagement ordermanagement = new Ordermanagement();

  private final Projectmanagement projectmanagement = new Projectmanagement();

  private final ReportTask reportTask = new ReportTask();

  private final Seo4ajax seo4ajax = new Seo4ajax();

  /**
   * Configure the current elasticsearch server version for testing.
   *
   * @author René Reitmann
   */
  @Getter
  @Setter
  public static class Elasticsearch {
    public static final String TEST_VERSION = "7.12.1";
    private String version = "7.12.1";
  }

  /**
   * Configuration Properties for the Angular Elasticsearch Client.
   *
   * @author Amine Limouri
   */
  @Getter
  @Setter
  public static class ElasticsearchAngularClient {
    // default connection url
    private String url = "http://localhost:9200";
    private String apiVersion = "7.3";
    private String logLevel = "trace";
    private Integer pageSize = 10;
  }

  /**
   * Configuration Properties for Dara.
   *
   * @author Daniel Katzberg
   */
  @Getter
  @Setter
  public static class Dara {
    private String endpoint;
    private String username;
    private String password;
  }

  /**
   * Credentials for da|ra variables registration (PID).
   */
  @Getter
  @Setter
  public static class DaraPid {
    private boolean enabled;
    private long statusPollIntervalInSeconds;
    private long abortAfterMinutesElapsed;
    private String endpoint;
    private String username;
    private String password;
  }

  /**
   * Configuration Properties for DataCite DOI registration.
   */
  @Getter
  @Setter
  public static class DataCite {
    private String endpoint;
    private String username;
    private String password;
  }

  /**
   * SSL CA certificate for MongoDB.
   */
  @Getter
  @Setter
  public static class Mongodb {
    private String sslCaCertificate;
  }

  /**
   * Custom Server properties.
   *
   * @author René Reitmann
   */
  @Getter
  @Setter
  public static class Server {
    private String contextRoot = null;

    private Integer instanceIndex = null;
  }

  /**
   * Custom Order Management properties.
   *
   * @author René Reitmann
   */
  @Getter
  @Setter
  public static class Ordermanagement {
    private String email = "";
  }

  /**
   * Project Management properties.
   */
  @Getter
  @Setter
  public static class Projectmanagement {
    private String email = "";
  }

  /**
   * DLP properties.
   */
  @Getter
  @Setter
  public static class Dlp {
    private String endpoint;
  }

  /**
   * Properties for the report-task.
   */
  @Getter
  @Setter
  public static class ReportTask {
    private String startCommand;
    private String taskDefinition;
    private String clusterName;
    private String serviceName;
    private String containerName;
  }

  /**
   * Properties for seo4ajax.
   */
  @Getter
  @Setter
  public static class Seo4ajax {
    private String siteToken;
    private String regexpBots;
    private String apiUrl;
  }
}
