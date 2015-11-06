package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

import java.io.Serializable;

/**
 * A dto representation of a field error.
 */
public class FieldErrorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String objectName;

  private final String field;

  private final String message;

  FieldErrorDto(String dto, String field, String message) {
    this.objectName = dto;
    this.field = field;
    this.message = message;
  }

  public String getObjectName() {
    return objectName;
  }

  public String getField() {
    return field;
  }

  public String getMessage() {
    return message;
  }

}
