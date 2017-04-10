package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * Configure Spring Data Rest to validate on PUT and POST.
 * Spring data rest converts the validation message in json and send a 400 when validation fails.
 * 
 * @author René Reitmann
 */
@Configuration
public class RepositoryRestConfiguration extends RepositoryRestConfigurerAdapter {

  @Autowired
  private LocalValidatorFactoryBean validator;

  @Override
  public void configureValidatingRepositoryEventListener(
      ValidatingRepositoryEventListener validatingListener) {
    validatingListener.addValidator("beforeCreate", validator);
    validatingListener.addValidator("beforeSave", validator);
  }
}
