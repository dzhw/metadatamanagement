package eu.dzhw.fdz.metadatamanagement.common.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import eu.dzhw.fdz.metadatamanagement.common.rest.filter.CachingHttpHeadersFilter;

/**
 * Properties specific to JHipster.
 *
 * <p>
 * Properties are configured in the application.yml file.
 * </p>
 */
@ConfigurationProperties(prefix = "jhipster", ignoreUnknownFields = false)
public class JHipsterProperties {

  private final Http http = new Http();

  private final Cache cache = new Cache();

  private final Mail mail = new Mail();

  private final Security security = new Security();

  private final Metrics metrics = new Metrics();

  public Http getHttp() {
    return http;
  }

  public Cache getCache() {
    return cache;
  }

  public Mail getMail() {
    return mail;
  }

  public Security getSecurity() {
    return security;
  }

  public Metrics getMetrics() {
    return metrics;
  }

  /**
   * Configuration for the {@link CachingHttpHeadersFilter}.
   */
  public static class Http {

    private final Cache cache = new Cache();

    public Cache getCache() {
      return cache;
    }

    /**
     * Configuration for the {@link CachingHttpHeadersFilter}.
     */
    public static class Cache {

      private int timeToLiveInDays = 31;

      public int getTimeToLiveInDays() {
        return timeToLiveInDays;
      }

      public void setTimeToLiveInDays(int timeToLiveInDays) {
        this.timeToLiveInDays = timeToLiveInDays;
      }
    }
  }

  /**
   * Configuration for the {@link CachingHttpHeadersFilter}.
   */
  public static class Cache {

    private int timeToLiveSeconds = 3600;

    public int getTimeToLiveSeconds() {
      return timeToLiveSeconds;
    }

    public void setTimeToLiveSeconds(int timeToLiveSeconds) {
      this.timeToLiveSeconds = timeToLiveSeconds;
    }
  }

  /**
   * Configuration of the smtp server connection.
   */
  public static class Mail {

    private String from = "metadatamanagement@localhost";

    public String getFrom() {
      return from;
    }

    public void setFrom(String from) {
      this.from = from;
    }
  }

  /**
   * JHipster Security configuration.
   */
  public static class Security {

    private final Rememberme rememberme = new Rememberme();

    private final Authentication authentication = new Authentication();

    public Rememberme getRememberme() {
      return rememberme;
    }

    public Authentication getAuthentication() {
      return authentication;
    }

    /**
     * JHipster Authentication configuration.
     */
    public static class Authentication {

      private final Oauth oauth = new Oauth();

      public Oauth getOauth() {
        return oauth;
      }

      /**
       * JHipster oauth configuration.
       */
      public static class Oauth {

        private String clientid;

        private String secret;

        @SuppressFBWarnings("NM_CONFUSING")
        public String getClientid() {
          return clientid;
        }

        public void setClientid(String clientid) {
          this.clientid = clientid;
        }

        public String getSecret() {
          return secret;
        }

        public void setSecret(String secret) {
          this.secret = secret;
        }
      }
    }
    
    /**
     * JHipster rememberme configuration.
     */
    public static class Rememberme {

      @NotNull
      private String key;

      public String getKey() {
        return key;
      }

      public void setKey(String key) {
        this.key = key;
      }
    }
  }

  /**
   * Configure metrics reporting.
   */
  public static class Metrics {

    private final Jmx jmx = new Jmx();

    private final Spark spark = new Spark();

    private final Graphite graphite = new Graphite();

    public Jmx getJmx() {
      return jmx;
    }

    public Spark getSpark() {
      return spark;
    }

    public Graphite getGraphite() {
      return graphite;
    }

    /**
     * Configure jmx.
     */
    public static class Jmx {

      private boolean enabled = true;

      public boolean isEnabled() {
        return enabled;
      }

      public void setEnabled(boolean enabled) {
        this.enabled = enabled;
      }
    }
    
    /**
     * Configure Spark.
     */
    public static class Spark {

      private boolean enabled = false;

      private String host = "localhost";

      private int port = 9999;

      public boolean isEnabled() {
        return enabled;
      }

      public void setEnabled(boolean enabled) {
        this.enabled = enabled;
      }

      public String getHost() {
        return host;
      }

      public void setHost(String host) {
        this.host = host;
      }

      public int getPort() {
        return port;
      }

      public void setPort(int port) {
        this.port = port;
      }
    }

    /**
     * Configure Graphite.
     */
    public static class Graphite {

      private boolean enabled = false;

      private String host = "localhost";

      private int port = 2003;

      private String prefix = "metadatamanagement";

      public boolean isEnabled() {
        return enabled;
      }

      public void setEnabled(boolean enabled) {
        this.enabled = enabled;
      }

      public String getHost() {
        return host;
      }

      public void setHost(String host) {
        this.host = host;
      }

      public int getPort() {
        return port;
      }

      public void setPort(int port) {
        this.port = port;
      }

      public String getPrefix() {
        return prefix;
      }

      public void setPrefix(String prefix) {
        this.prefix = prefix;
      }
    }
  }
}
