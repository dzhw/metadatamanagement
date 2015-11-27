package eu.dzhw.fdz.metadatamanagement.service.exception;

/**
 * This exception is thrown when for instance an entity needs to be updated but is not found.
 * 
 * @author René Reitmann
 */
public class EntityNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -1608161733326109527L;
  private Class<?> entityClass;
  private String entityId;

  /**
   * Create the exception with the enities type and its id.
   * @param entityClass The class of the entity which was not found.
   * @param entityId The id of the entity which was not found.
   */
  public EntityNotFoundException(Class<?> entityClass, String entityId) {
    super("Entity of type " + entityClass.getSimpleName() + " with ID=" + entityId + " not found!");
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
