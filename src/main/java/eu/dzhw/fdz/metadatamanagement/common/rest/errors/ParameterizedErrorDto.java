package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.Serializable;

/**
 * DTO for sending a parameterized error message.
 */
public class ParameterizedErrorDto implements Serializable {

  private static final long serialVersionUID = 1L;
  private final String message;
  private final String[] params;

  public ParameterizedErrorDto(String message, String... params) {
    this.message = message;
    this.params = params;
  }

  public String getMessage() {
    return message;
  }

  public String[] getParams() {
    return params.clone();
  }

}
