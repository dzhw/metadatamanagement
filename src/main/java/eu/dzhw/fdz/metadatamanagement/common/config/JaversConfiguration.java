package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Locale;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.MappingStyle;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.repository.api.JaversRepository;
import org.javers.repository.mongo.MongoRepository;
import org.javers.spring.boot.mongo.JaversMongoAutoConfiguration;
import org.javers.spring.boot.mongo.JaversProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Setting cache size for javers snapshots to zero. Heavily copied from
 * {@link JaversMongoAutoConfiguration}.
 * 
 * @author René Reitmann
 */
@Configuration
@EnableConfigurationProperties({JaversProperties.class})
public class JaversConfiguration {

  @Autowired
  private JaversProperties javersProperties;

  @Autowired
  private MongoClient mongoClient;

  @Autowired
  private MongoProperties mongoProperties;

  /**
   * Creating {@link Javers} bean without caching.
   * 
   * @return The {@link Javers} bean.
   */
  @Bean(name = "javers")
  public Javers javers() {
    MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoProperties.getMongoClientDatabase());

    // setting cache size to zero to be able to clear the javers mongo db collections
    JaversRepository javersRepository = new MongoRepository(mongoDatabase, 0);

    return JaversBuilder.javers()
        .withListCompareAlgorithm(
            ListCompareAlgorithm.valueOf(
                javersProperties.getAlgorithm().toUpperCase(Locale.ROOT)))
        .withMappingStyle(MappingStyle.valueOf(
            javersProperties.getMappingStyle().toUpperCase(Locale.ROOT)))
        .withNewObjectsSnapshot(javersProperties.isNewObjectSnapshot())
        .withPrettyPrint(javersProperties.isPrettyPrint())
        .withTypeSafeValues(javersProperties.isTypeSafeValues())
        .registerJaversRepository(javersRepository)
        .withPackagesToScan(javersProperties.getPackagesToScan()).build();
  }
}
