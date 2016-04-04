package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mongeez.Mongeez;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.Mongo;

import eu.dzhw.fdz.metadatamanagement.common.config.oauth2.OAuth2AuthenticationReadConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.DateToLocalDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.DateToLocalDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.DateToZonedDateTimeConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.LocalDateTimeToDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.LocalDateToDateConverter;
import eu.dzhw.fdz.metadatamanagement.domain.util.Jsr310DateConverters.ZonedDateTimeToDateConverter;

/**
 * Configure the mongo db client instance when not running in the cloud.
 */
// TODO ensure that the mongo client configuration is the same as CloudMongoDbConfiguration
@Configuration
@Profile("!" + Constants.SPRING_PROFILE_CLOUD)
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.**.repository")
@Import(value = MongoAutoConfiguration.class)
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class DatabaseConfiguration extends AbstractMongoConfiguration {

  private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

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

  @Override
  protected String getDatabaseName() {
    return mongoProperties.getDatabase();
  }

  @Override
  public Mongo mongo() throws Exception {
    return mongo;
  }

  /**
   * Register custom converters for mongo access.
   */
  @Override
  @Bean
  public CustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();
    converters.add(new OAuth2AuthenticationReadConverter());
    converters.add(DateToZonedDateTimeConverter.INSTANCE);
    converters.add(ZonedDateTimeToDateConverter.INSTANCE);
    converters.add(DateToLocalDateConverter.INSTANCE);
    converters.add(LocalDateToDateConverter.INSTANCE);
    converters.add(DateToLocalDateTimeConverter.INSTANCE);
    converters.add(LocalDateTimeToDateConverter.INSTANCE);
    return new CustomConversions(converters);
  }

  /**
   * Configure Mongeez for schema management.
   */
  @Bean
  @Profile("!" + Constants.SPRING_PROFILE_FAST)
  public Mongeez mongeez() {
    log.debug("Configuring Mongeez");
    Mongeez mongeez = new Mongeez();
    mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
    mongeez.setMongo(mongo);
    mongeez.setDbName(mongoProperties.getDatabase());
    mongeez.process();
    return mongeez;
  }

  @Bean
  public GridFsTemplate gridFsTemplate() throws Exception {
    return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
  }
}
