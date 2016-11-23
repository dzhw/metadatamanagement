package eu.dzhw.fdz.metadatamanagement.common.domain.exception;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.CustomParameterizedException;

/**
 * This exception is thrown when the user tries to create an entity in mongo which id is already
 * used.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public class EntityExistsException extends CustomParameterizedException {

  private static final long serialVersionUID = -1068483913940981111L;

  /**
   * Generate the exception.
   * @param entityClass the class of the entity which already exists
   * @param entityId the id of the entity which already exists
   */
  public EntityExistsException(Class<?> entityClass, String entityId) {
    super("entity.exist.DEMO.String", entityId, null, entityClass.getSimpleName());
  }
  
  /**
   * Generate the exception.
   * @param entityClass the class of the entity which already exists
   * @param fields the field errors
   */
  public EntityExistsException(Class<?> entityClass, String[] fields) {    
    super("entity.compound, exist.DEMO.String", "testId", null, entityClass.getSimpleName());
    for (String field : fields) {
      System.out.println(field);
    }
  }
}