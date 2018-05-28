package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PreDestroy;

import org.mongeez.Mongeez;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.gridfs.GridFS;

import lombok.extern.slf4j.Slf4j;

/**
 * Configure the mongo db client instance. 
 * Modified version of {@link MongoAutoConfiguration}.
 */
@Configuration
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.**.repository")
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
@Slf4j
public class MongoDbConfiguration extends AbstractMongoConfiguration {
  private MongoProperties mongoProperties;
  
  private MongoClientOptions options;

  private MongoClient mongo;
  
  private MongoClientFactory factory;
  
  /**
   * Constructor from {@link MongoAutoConfiguration}.
   */
  @Autowired
  public MongoDbConfiguration(MongoProperties properties,
      ObjectProvider<MongoClientOptions> options, Environment environment) {
    this.mongoProperties = properties;
    this.options = options.getIfAvailable();
    this.factory = new MongoClientFactory(properties, environment);
  }

  /**
   * Close the mongo client gracefully.
   */
  @PreDestroy
  public void close() {
    if (this.mongo != null) {
      this.mongo.close();
    }
  }

  /**
   * JHipster enabled validation on repository layer.
   */
  @Bean
  public ValidatingMongoEventListener validatingMongoEventListener(
      LocalValidatorFactoryBean validator) {
    return new ValidatingMongoEventListener(validator);
  }

  /**
   * We need to set springs standard message source for the JSR-303 validation errors.
   */
  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.setValidationMessageSource(messageSource);
    return validator;
  }

  /**
   * Configure Mongeez for schema management.
   * 
   * @deprecated It is not working on cloudfoundry!
   */
  @Bean
  @Profile({Constants.SPRING_PROFILE_LOCAL, Constants.SPRING_PROFILE_UNITTEST})
  public Mongeez mongeez(Mongo mongo) {
    log.debug("Configuring Mongeez");
    Mongeez mongeez = new Mongeez();
    mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
    mongeez.setMongo(mongo);
    mongeez.setDbName(mongoProperties.getMongoClientDatabase());
    mongeez.process();
    return mongeez;
  }

  @Override
  protected String getDatabaseName() {
    return mongoProperties.getMongoClientDatabase();
  }

  @Bean
  @ConditionalOnMissingBean
  @Override
  public MongoClient mongoClient() {
    mongo = this.factory.createMongoClient(this.options);
    return mongo;
  }
  
  @Bean
  public GridFS gridFs(MongoClient mongoClient) {
    return new GridFS(mongoClient.getDB(this.getDatabaseName()));
  }
  
  @Override
  protected Collection<String> getMappingBasePackages() {
    return Arrays.asList("eu.dzhw.fdz.metadatamanagement.**.domain");
  }
}