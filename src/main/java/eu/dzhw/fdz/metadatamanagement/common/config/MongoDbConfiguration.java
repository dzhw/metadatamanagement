package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.HashSet;
import java.util.Set;

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
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.github.cloudyrock.spring.v5.EnableMongock;

/**
 * Configure the mongo db client instance. Modified version of {@link MongoAutoConfiguration}.
 */
@Configuration
@EnableMongoRepositories("eu.dzhw.fdz.metadatamanagement.**.repository")
@EnableMongoAuditing(auditorAwareRef = "auditorStoreAware")
@EnableMongock
public class MongoDbConfiguration {

  private final Environment environment;

  private final ResourceLoader resourceLoader;

  /**
   * Constructor from {@link MongoAutoConfiguration}.
   */
  @Autowired
  public MongoDbConfiguration(Environment environment,
      ResourceLoader resourceLoader) {
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
   * Create the {@link MongoMappingContext} which scans only our domain objects.
   *
   * @param applicationContext The application context
   * @param properties The {@link MongoProperties}
   * @param conversions Registered converters
   * @return the {@link MongoMappingContext}
   */
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
   */
  private Set<Class<?>> getInitialEntitySet(String... basePackages) throws ClassNotFoundException {

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
