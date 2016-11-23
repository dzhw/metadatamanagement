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

  private final List<ErrorDto> errors = new ArrayList<>();

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
      this.errors.add(new ErrorDto(globalError.getObjectName(), message, null, null));
    }
    
    for (FieldError fieldError : violationException.getErrors()
        .getFieldErrors()) {

      String message = accessor.getMessage(fieldError);

      this.errors.add(new ErrorDto(fieldError.getObjectName(), message,
          String.format("%s", fieldError.getRejectedValue()), fieldError.getField()));
    }
  }

  @JsonProperty("errors")
  public List<ErrorDto> getErrors() {
    return errors;
  }
}
