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
  
  public JsonParsingError(String errorMessage) {
    errors.add(new Error(errorMessage));
  }
  
  public List<Error> getErrors() {
    return this.errors;
  }

  /**
   * Wrapper for the error message.
   */
  public static class Error {
    public Error(String errorMessage) {
      this.message = errorMessage;
    }

    private String message;

    public String getMessage() {
      return message;
    }
  }
}
