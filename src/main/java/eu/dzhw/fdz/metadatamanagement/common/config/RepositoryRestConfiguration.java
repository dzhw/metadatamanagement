package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
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
   * @param persistentEntitiesFactory see {@link ObjectFactory}
   * @param validator the JSR-303 validator
   * 
   * @return The bean to register
   */
  @Bean
  public PrioritizedValidatingRepositoryEventListener prioritizedValidatingRepositoryEventListener(
      ObjectFactory<PersistentEntities> persistentEntitiesFactory,
      LocalValidatorFactoryBean validator) {
    PrioritizedValidatingRepositoryEventListener prioritizedValidatingListener = 
        new PrioritizedValidatingRepositoryEventListener(persistentEntitiesFactory);
    prioritizedValidatingListener.addValidator("beforeCreate", validator);
    prioritizedValidatingListener.addValidator("beforeSave", validator);
    return prioritizedValidatingListener;
  }
}
