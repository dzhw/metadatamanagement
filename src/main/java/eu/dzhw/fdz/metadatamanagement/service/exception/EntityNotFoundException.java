package eu.dzhw.fdz.metadatamanagement.service.exception;

/**
 * This exception is thrown when for instance an entity needs to be updated but is not found.
 * 
 * @author René Reitmann
 */
public class EntityNotFoundException extends Exception {

  private static final long serialVersionUID = -1608161733326109527L;

  public EntityNotFoundException(Class<?> entityClass, String id) {
    super("Entity of type " + entityClass.getSimpleName() + " with ID=" + id + " not found!");
  }
}
