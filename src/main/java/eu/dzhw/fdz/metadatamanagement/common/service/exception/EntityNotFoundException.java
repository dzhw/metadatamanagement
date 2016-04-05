package eu.dzhw.fdz.metadatamanagement.common.service.exception;

import eu.dzhw.fdz.metadatamanagement.web.rest.errors.CustomParameterizedException;
import eu.dzhw.fdz.metadatamanagement.web.rest.errors.ErrorConstants;

/**
 * This exception is thrown when for instance an entity needs to be updated but is not found.
 * 
 * @author René Reitmann
 */
public class EntityNotFoundException extends CustomParameterizedException {

  private static final long serialVersionUID = -1608161733326109527L;

  /**
   * Create the exception with the enities type and its id.
   * @param entityClass The class of the entity which was not found.
   * @param entityId The id of the entity which was not found.
   */
  public EntityNotFoundException(Class<?> entityClass, String entityId) {
    super(ErrorConstants.ERR_ENTITY_NOT_FOUND,entityClass.getSimpleName(),entityId);
  }
}
