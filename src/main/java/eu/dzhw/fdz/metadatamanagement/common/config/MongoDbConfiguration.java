package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mongeez.Mongeez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;

import eu.dzhw.fdz.metadatamanagement.usermanagement.repository.OAuth2AuthenticationReadConverter;

/**
 * Configure the mongo db client instance.
 */
@Configuration
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.**.repository")
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class MongoDbConfiguration {

  private final Logger log = LoggerFactory.getLogger(MongoDbConfiguration.class);

  @Inject
  private Mongo mongo;

  @Inject
  private MongoProperties mongoProperties;

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
   * Register custom converters for mongo access.
   */
  @Bean
  public CustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();
    converters.add(new OAuth2AuthenticationReadConverter());
    return new CustomConversions(converters);
  }

  /**
   * Configure Mongeez for schema management.
   */
  @Bean
  @Profile({Constants.SPRING_PROFILE_LOCAL, Constants.SPRING_PROFILE_UNITTEST})
  public Mongeez mongeez() {
    log.debug("Configuring Mongeez");
    Mongeez mongeez = new Mongeez();
    mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
    mongeez.setMongo(mongo);
    mongeez.setDbName(mongoProperties.getDatabase());
    mongeez.process();
    return mongeez;
  }
}