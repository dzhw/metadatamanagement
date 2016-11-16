package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Error DTO which is returned when a client sends a json which cannot be converted to a Java
 * Object.
 * 
 * @author René Reitmann
 */
public class JsonParsingError {
  List<Error> errors = new ArrayList<>();
  
  public JsonParsingError(String errorMessage, String entity, 
      String invalidValue, String property) {
    errors.add(new Error(errorMessage, entity, invalidValue, property));
  }
  
  public List<Error> getErrors() {
    return this.errors;
  }

  /**
   * Wrapper for the error message.
   */
  public static class Error {
    
    
    /**
     * The complete constructor for a error message.
     * @param errorMessage The key/path for the i18n json files
     * @param entity The "domain class" where happens the error
     * @param invalidValue The invalid value.
     * @param property The field where has an invalid value.
     */
    public Error(String errorMessage, String entity, String invalidValue, String property) {
      this.message = errorMessage;
      this.entity = entity;
      this.invalidValue = invalidValue;
      this.property = property;
    }

    private final String entity;
    private final String message;
    private final String invalidValue;
    private final String property;

    public String getMessage() {
      return message;
    }

    public String getEntity() {
      return entity;
    }

    public String getInvalidValue() {
      return invalidValue;
    }

    public String getProperty() {
      return property;
    }
    
    
  }
}
