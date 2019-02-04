package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
}
