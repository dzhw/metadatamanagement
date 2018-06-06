package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.stereotype.Component;

/**
 * This validator ensures that validation runs before Spring Data Rests
 * {@link HandleBeforeSave} and {@link HandleBeforeCreate} are triggered.
 * 
 * @author René Reitmann
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PrioritizedValidatingRepositoryEventListener 
    extends ValidatingRepositoryEventListener {
  
  public PrioritizedValidatingRepositoryEventListener(
      ObjectFactory<PersistentEntities> persistentEntitiesFactory) {
    super(persistentEntitiesFactory);
  }
}
