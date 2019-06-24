package eu.dzhw.fdz.metadatamanagement.common.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractRdcDomainObject;

/**
 * Configure Spring Data Rest to validate on PUT and POST. Spring data rest converts the validation
 * message in json and send a 400 when validation fails.
 * 
 * @author René Reitmann
 */
@Component
public class RestRepositoryConfigurer implements RepositoryRestConfigurer {

  @Autowired
  private LocalValidatorFactoryBean validator;

  @Autowired
  private PrioritizedValidatingRepositoryEventListener prioritizedValidatingRepositoryEventListener;

  /**
   * Register a custom validator which validates incoming REST requests before triggering
   * {@link HandleBeforeSave} and {@link HandleBeforeCreate}.
   */
  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    prioritizedValidatingRepositoryEventListener.addValidator("beforeCreate", validator);
    prioritizedValidatingRepositoryEventListener.addValidator("beforeSave", validator);
  }

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    final ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);
    provider.addIncludeFilter(new AssignableTypeFilter(AbstractRdcDomainObject.class));

    final Set<BeanDefinition> beans =
        provider.findCandidateComponents("eu.dzhw.fdz.metadatamanagement.**.domain");

    for (final BeanDefinition bean : beans) {
      try {
        config.exposeIdsFor(Class.forName(bean.getBeanClassName()));
      } catch (final ClassNotFoundException e) {
        // Can't throw ClassNotFoundException due to the method signature. Need to cast it
        throw new IllegalStateException("Failed to expose `id` field due to", e);
      }
    }
  }
}
