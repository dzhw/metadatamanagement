package eu.dzhw.fdz.metadatamanagement.service.exception;

/**
 * This exception is thrown when the user tries to create an entity in mongo which id is already
 * used.
 * 
 * @author René Reitmann
 */
public class EntityExistsException extends RuntimeException {

  private static final long serialVersionUID = -1068483913940981111L;
  private Class<?> entityClass;
  private String entityId;

  /**
   * Generate the exception.
   * @param entityClass the class of the entity which already exists
   * @param entityId the id of the entity which already exists
   * @param cause root cause exception
   */
  public EntityExistsException(Class<?> entityClass, String entityId, Throwable cause) {
    super("Entity of type " + entityClass.getSimpleName() + " with ID=" + entityId
        + " already exists!", cause);
    this.entityClass = entityClass;
    this.entityId = entityId;
  }

  public Class<?> getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(Class<?> entityClass) {
    this.entityClass = entityClass;
  }

  public String getEntityId() {
    return entityId;
  }

  public void setEntityId(String entityId) {
    this.entityId = entityId;
  }
}
