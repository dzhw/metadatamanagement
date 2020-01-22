package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.HashSet;
import java.util.Set;

import org.mongeez.Mongeez;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.mongodb.gridfs.GridFS;

import lombok.extern.slf4j.Slf4j;

/**
 * Configure the mongo db client instance. Modified version of {@link MongoAutoConfiguration}.
 */
@Configuration
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.**.repository")
@EnableMongoAuditing(auditorAwareRef = "springSecurityAuditorAware")
@Slf4j
public class MongoDbConfiguration {

  private final MongoDbFactory mongo;

  private final Environment environment;
  
  private final ResourceLoader resourceLoader;
  /**
   * Constructor from {@link MongoAutoConfiguration}.
   */
  @Autowired
  public MongoDbConfiguration(MongoDbFactory mongo, Environment environment,
      ResourceLoader resourceLoader) {
    this.mongo = mongo;
    this.environment = environment;
    this.resourceLoader = resourceLoader;
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
  @Deprecated
  @Bean
  @Profile({Constants.SPRING_PROFILE_LOCAL, Constants.SPRING_PROFILE_UNITTEST})
  public Mongeez mongeez() {
    log.debug("Configuring Mongeez");
    Mongeez mongeez = new Mongeez();
    mongeez.setFile(new ClassPathResource("/config/mongeez/master.xml"));
    mongeez.setMongo(mongo.getLegacyDb().getMongo());
    mongeez.setDbName(mongo.getDb().getName());
    mongeez.process();
    return mongeez;
  }

  @Bean
  public GridFS gridFs() {
    return new GridFS(mongo.getLegacyDb());
  }

  @Bean
  public MongoMappingContext mongoMappingContext(ApplicationContext applicationContext,
      MongoProperties properties, MongoCustomConversions conversions)
      throws ClassNotFoundException {
    PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
    MongoMappingContext context = new MongoMappingContext();
    mapper.from(properties.isAutoIndexCreation()).to(context::setAutoIndexCreation);
    context.setInitialEntitySet(getInitialEntitySet("eu.dzhw.fdz.metadatamanagement.**.domain"));
    Class<?> strategyClass = properties.getFieldNamingStrategy();
    if (strategyClass != null) {
      context
          .setFieldNamingStrategy((FieldNamingStrategy) BeanUtils.instantiateClass(strategyClass));
    }
    context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
    return context;
  }

  /**
   * Scans the mapping base package for classes annotated with {@link Document}.
   * 
   * @see #getMappingBasePackage()
   * @return
   * @throws ClassNotFoundException
   */
  private Set<Class<?>> getInitialEntitySet(String... basePackages)
      throws ClassNotFoundException {

    Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();

    ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);
    provider.setEnvironment(environment);
    provider.setResourceLoader(resourceLoader);
    provider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
    provider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

    for (String basePackage : basePackages) {
      if (StringUtils.hasText(basePackage)) {
        for (BeanDefinition candidate : provider.findCandidateComponents(basePackage)) {
          initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
              MongoDbConfiguration.class.getClassLoader()));
        }
      }
    }

    return initialEntitySet;
  }
}
