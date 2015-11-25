package eu.dzhw.fdz.metadatamanagement.service.exception;

/**
 * This exception is thrown when the user tries to create an entity in mongo which id is already
 * used.
 * 
 * @author René Reitmann
 */
public class EntityExistsException extends Exception {

  private static final long serialVersionUID = -1068483913940981111L;

  public EntityExistsException(Class<?> entityClass, String id, Throwable cause) {
    super("Entity of type " + entityClass.getSimpleName() + " with ID=" + id + " already exists!",
        cause);
  }


}
