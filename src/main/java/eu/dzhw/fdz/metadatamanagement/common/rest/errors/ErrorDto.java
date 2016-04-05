package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transfering error message with a list of field errors.
 */
public class ErrorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final String description;

  private List<FieldErrorDto> fieldErrors;

  ErrorDto(String message) {
    this(message, null);
  }

  ErrorDto(String message, String description) {
    this.message = message;
    this.description = description;
  }

  ErrorDto(String message, String description, List<FieldErrorDto> fieldErrors) {
    this.message = message;
    this.description = description;
    this.fieldErrors = fieldErrors;
  }

  /**
   * Add another field error.
   */
  public void add(String objectName, String field, String message) {
    if (fieldErrors == null) {
      fieldErrors = new ArrayList<>();
    }
    fieldErrors.add(new FieldErrorDto(objectName, field, message));
  }

  public String getMessage() {
    return message;
  }

  public String getDescription() {
    return description;
  }

  public List<FieldErrorDto> getFieldErrors() {
    return fieldErrors;
  }
}
