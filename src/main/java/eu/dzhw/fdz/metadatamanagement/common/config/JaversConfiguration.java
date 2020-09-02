package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Locale;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.MappingStyle;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.repository.api.JaversRepository;
import org.javers.repository.mongo.MongoRepository;
import org.javers.spring.boot.mongo.JaversMongoAutoConfiguration;
import org.javers.spring.boot.mongo.JaversMongoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.client.MongoClient;

/**
 * Setting cache size for javers snapshots to zero. Heavily copied from
 * {@link JaversMongoAutoConfiguration}.
 *
 * @author René Reitmann
 */
@Configuration
@EnableConfigurationProperties({JaversMongoProperties.class})
public class JaversConfiguration {

  @Autowired
  private JaversMongoProperties javersMongoProperties;

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
    // setting cache size to zero to be able to clear the javers mongo db collections
    JaversRepository javersRepository =
        new MongoRepository(mongoClient.getDatabase(mongoProperties.getDatabase()), 0);

    return JaversBuilder.javers()
        .withListCompareAlgorithm(ListCompareAlgorithm
            .valueOf(javersMongoProperties.getAlgorithm().toUpperCase(Locale.ROOT)))
        .withMappingStyle(
            MappingStyle.valueOf(javersMongoProperties.getMappingStyle().toUpperCase(Locale.ROOT)))
        .withNewObjectsSnapshot(javersMongoProperties.isNewObjectSnapshot())
        .withPrettyPrint(javersMongoProperties.isPrettyPrint())
        .withTypeSafeValues(javersMongoProperties.isTypeSafeValues())
        .registerJaversRepository(javersRepository)
        .withPackagesToScan(javersMongoProperties.getPackagesToScan()).build();
  }
}
