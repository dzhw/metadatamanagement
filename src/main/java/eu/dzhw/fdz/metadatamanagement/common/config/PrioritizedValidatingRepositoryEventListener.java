package eu.dzhw.fdz.metadatamanagement.common.config;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;

/**
 * This validator ensures that validation runs before Spring Data Rests
 * {@link BeforeSaveEvent} are triggered.
 * 
 * @author René Reitmann
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PrioritizedValidatingRepositoryEventListener 
    extends ValidatingRepositoryEventListener {
  
  public PrioritizedValidatingRepositoryEventListener(
      ObjectFactory<PersistentEntities> persistentEntitiesFactory) {
    super(persistentEntitiesFactory);
  }
}
