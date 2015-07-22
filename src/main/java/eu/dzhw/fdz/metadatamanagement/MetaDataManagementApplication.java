package eu.dzhw.fdz.metadatamanagement;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main class of our application which starts the spring boot microservice.
 * 
 * @author René Reitmann
 */
@SpringBootApplication
public class MetaDataManagementApplication {

  /**
   * Starts the microservice and remembers the startup time.
   * 
   * @param args Currently no args are supported.
   */
  public static void main(final String[] args) {
    // TODO should be replaced by jmx property
    System.setProperty("info.app.startuptime", new Date().toString());
    SpringApplication.run(MetaDataManagementApplication.class, args);
  }
}
