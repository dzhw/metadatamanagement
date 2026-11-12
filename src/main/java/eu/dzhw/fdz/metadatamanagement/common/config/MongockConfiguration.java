package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.ReadConcern;
import com.mongodb.ReadConcernLevel;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;

import io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver;
import io.mongock.runner.standalone.MongockStandalone;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Runs the Mongock migrations on startup.
 *
 * <p>Mongock does not provide a Spring Boot 4 integration, therefore the standalone runner is used
 * with the plain MongoDB sync driver. It is started as {@link ApplicationRunner} like the former
 * {@code @EnableMongock} default and is configured from the same {@code mongock.*} properties. The
 * change logs only use the raw {@link com.mongodb.client.MongoDatabase}, so no Spring Data
 * integration is needed. The default collection names ({@code mongockChangeLog},
 * {@code mongockLock}) are kept, so already executed change sets are not executed again.</p>
 */
@Configuration
@EnableConfigurationProperties(MongockConfiguration.MongoDbProperties.class)
@Slf4j
public class MongockConfiguration {

  /**
   * Mongock settings bound from {@code mongock.*} (e.g. enabled, migration-scan-package,
   * transaction-enabled).
   */
  @Bean
  @ConfigurationProperties("mongock")
  io.mongock.api.config.MongockConfiguration mongockProperties() {
    return new io.mongock.api.config.MongockConfiguration();
  }

  /**
   * Execute all pending change sets once the application context is ready.
   */
  @Bean
  ApplicationRunner mongockApplicationRunner(MongoClient mongoClient, MongoTemplate mongoTemplate,
      io.mongock.api.config.MongockConfiguration mongockProperties,
      MongoDbProperties mongoDbProperties) {
    return args -> {
      if (!mongockProperties.isEnabled()) {
        log.info("Mongock is disabled, skipping database migrations");
        return;
      }
      MongoSync4Driver driver =
          MongoSync4Driver.withDefaultLock(mongoClient, mongoTemplate.getDb().getName());
      mongoDbProperties.applyTo(driver);
      MongockStandalone.builder()
          .setDriver(driver)
          .setConfig(mongockProperties)
          .buildRunner()
          .execute();
    };
  }

  /**
   * Read/write concern settings for the Mongock driver ({@code mongock.mongo-db.*}).
   */
  @Data
  @ConfigurationProperties("mongock.mongo-db")
  public static class MongoDbProperties {
    private WriteConcernProperties writeConcern;

    private String readConcern;

    private String readPreference;

    void applyTo(MongoSync4Driver driver) {
      if (writeConcern != null && writeConcern.getW() != null) {
        WriteConcern concern = "majority".equalsIgnoreCase(writeConcern.getW())
            ? WriteConcern.MAJORITY : new WriteConcern(Integer.parseInt(writeConcern.getW()));
        if (writeConcern.getWTimeoutMs() != null) {
          concern = concern.withWTimeout(writeConcern.getWTimeoutMs(), TimeUnit.MILLISECONDS);
        }
        if (writeConcern.getJournal() != null) {
          concern = concern.withJournal(writeConcern.getJournal());
        }
        driver.setWriteConcern(concern);
      }
      if (readConcern != null) {
        driver.setReadConcern(new ReadConcern(ReadConcernLevel.fromString(readConcern)));
      }
      if (readPreference != null) {
        driver.setReadPreference(ReadPreference.valueOf(readPreference));
      }
    }

    /**
     * Write concern settings.
     */
    @Data
    public static class WriteConcernProperties {
      private String w;

      private Long wTimeoutMs;

      private Boolean journal;
    }
  }
}
