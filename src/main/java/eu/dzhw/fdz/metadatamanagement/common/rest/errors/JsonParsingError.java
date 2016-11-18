package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Error DTO which is returned when a client sends a json which cannot be converted to a Java
 * Object.
 * 
 * @author René Reitmann
 */
public class JsonParsingError {
  private List<Error> errors = new ArrayList<>();
  
  public JsonParsingError(String errorMessage, String entity, 
      String invalidValue, String property) {
    errors.add(new Error(entity, errorMessage, invalidValue, property));
  }
  
  @JsonProperty("errors")
  public List<Error> getErrors() {
    return this.errors;
  }
}
