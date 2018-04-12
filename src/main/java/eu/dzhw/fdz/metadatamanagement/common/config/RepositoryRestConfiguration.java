package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configure Spring Data Rest to validate on PUT and POST.
 * Spring data rest converts the validation message in json and send a 400 when validation fails.
 * 
 * @author René Reitmann
 */
@Configuration
public class RepositoryRestConfiguration {

  /**
   * Register a custom validator which validates incoming REST requests before throwing
   * {@link BeforeSaveEvent} and {@link BeforeCreateEvent}.
   * 
   * @param entities see {@link ObjectFactory}
   * @param validator the JSR-303 validator
   * 
   * @return The bean to register
   */
  @Bean
  public ValidatingRepositoryEventListener validatingRepositoryEventListener(
      ObjectFactory<PersistentEntities> entities,
      LocalValidatorFactoryBean validator) {
    PrioritizedValidatingRepositoryEventListener prioritizedValidatingListener = 
        new PrioritizedValidatingRepositoryEventListener(entities);
    prioritizedValidatingListener.addValidator("beforeCreate", validator);
    prioritizedValidatingListener.addValidator("beforeSave", validator);
    return prioritizedValidatingListener;
  }
}
