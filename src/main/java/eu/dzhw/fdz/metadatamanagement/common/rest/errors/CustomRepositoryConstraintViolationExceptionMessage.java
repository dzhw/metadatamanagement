package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.data.rest.webmvc.support.RepositoryConstraintViolationExceptionMessage;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Custom dto for validation errors of spring data rest repositories.
 * This is necessary due to issue #706. 
 * It is a modified copy of {@link RepositoryConstraintViolationExceptionMessage}.
 * 
 * @author René Reitmann
 *
 */
public class CustomRepositoryConstraintViolationExceptionMessage {

  private final List<ValidationError> errors = new ArrayList<ValidationError>();

  /**
   * Construct the list of error dtos.
   * 
   * @param violationException The validation exception.
   * @param accessor message source for internationalization.
   */
  public CustomRepositoryConstraintViolationExceptionMessage(
      RepositoryConstraintViolationException violationException, MessageSourceAccessor accessor) {

    for (ObjectError globalError : violationException.getErrors().getGlobalErrors()) {
      String message = accessor.getMessage(globalError);
      this.errors.add(new ValidationError(globalError.getObjectName(), message, null, null));
    }
    
    for (FieldError fieldError : violationException.getErrors()
        .getFieldErrors()) {

      String message = accessor.getMessage(fieldError);

      this.errors.add(new ValidationError(fieldError.getObjectName(), message,
          String.format("%s", fieldError.getRejectedValue()), fieldError.getField()));
    }
  }

  @JsonProperty("errors")
  public List<ValidationError> getErrors() {
    return errors;
  }

  /**
   * The error dto.
   * 
   * @author René Reitmann
   */
  public static class ValidationError {

    private final String entity;
    private final String message;
    private final String invalidValue;
    private final String property;

    /**
     * Construct the error dto.
     * @param entity the name of the entity
     * @param message the internationalized message
     * @param invalidValue the rejected value of the property
     * @param property the name of the property (empty for global errors)
     */
    public ValidationError(String entity, String message, String invalidValue, String property) {
      this.entity = entity;
      this.message = message;
      this.invalidValue = invalidValue;
      this.property = property;
    }

    public String getEntity() {
      return entity;
    }

    public String getMessage() {
      return message;
    }

    public String getInvalidValue() {
      return invalidValue;
    }

    public String getProperty() {
      return property;
    }
  }
}
