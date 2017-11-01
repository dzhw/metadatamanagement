package eu.dzhw.fdz.metadatamanagement;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import eu.dzhw.fdz.metadatamanagement.common.config.Constants;
import eu.dzhw.fdz.metadatamanagement.common.config.JHipsterProperties;
import eu.dzhw.fdz.metadatamanagement.common.config.MetadataManagementProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class boostrapping the application.
 */
@SpringBootApplication
@EnableConfigurationProperties({MetadataManagementProperties.class, JHipsterProperties.class})
@Slf4j
public class Application {
  @Autowired
  private Environment env;

  /**
   * Initializes metadatamanagement.
   * <p/>
   * Spring profiles can be configured with a program arguments
   * --spring.profiles.active=your-active-profile
   * <p/>
   * <p>
   * You can find more information on how profiles work with JHipster on
   * <a href="http://jhipster.github.io/profiles.html">http://jhipster.github.io/profiles.html</a>.
   * </p>
   */
  @PostConstruct
  public void initApplication() throws IOException {
    if (env.getActiveProfiles().length == 0) {
      log.warn("No Spring profile configured, running with default configuration");
    } else {
      log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
      Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
      if (activeProfiles.size() > 1) {
        log.error("You have misconfigured your application! "
            + "It should not run with more than one at the same time.");
      }
    }
  }

  /**
   * Main method, used to run the application.
   */
  public static void main(String[] args) throws UnknownHostException {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication app = new SpringApplication(Application.class);
    SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
    addDefaultProfile(app, source);
    Environment env = app.run(args)
        .getEnvironment();
    log.info(
        "Access URLs:\n----------------------------------------------------------\n\t"
            + "Local: \t\thttp://127.0.0.1:{}\n\t"
            + "External: \thttp://{}:{}\n----------------------------------------------------------",
        env.getProperty("server.port"), InetAddress.getLocalHost()
          .getHostAddress(),
        env.getProperty("server.port"));

  }

  /**
   * If no profile has been configured, set by default the "local" profile.
   */
  private static void addDefaultProfile(SpringApplication app,
      SimpleCommandLinePropertySource source) {
    if (!source.containsProperty("spring.profiles.active") && !System.getenv()
        .containsKey("SPRING_PROFILES_ACTIVE")) {

      app.setAdditionalProfiles(Constants.SPRING_PROFILE_LOCAL);
    }
  }
}
