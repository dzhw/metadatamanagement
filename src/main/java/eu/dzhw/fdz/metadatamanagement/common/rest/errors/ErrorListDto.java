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
public class ErrorListDto {
  private List<ErrorDto> errorDtos = new ArrayList<>();
  
  public ErrorListDto(String errorMessage, String entity, 
      String invalidValue, String property) {
    errorDtos.add(new ErrorDto(entity, errorMessage, invalidValue, property));
  }
  
  @JsonProperty("errors")
  public List<ErrorDto> getErrors() {
    return this.errorDtos;
  }
}
