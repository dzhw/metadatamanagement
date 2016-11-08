package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.util.List;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

  @ExceptionHandler(ConcurrencyFailureException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  @ResponseBody
  public ErrorDto processConcurencyError(ConcurrencyFailureException ex) {
    return new ErrorDto(ErrorConstants.ERR_CONCURRENCY_FAILURE);
  }

  /**
   * Handle validation errors.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorDto processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return processFieldErrors(fieldErrors);
  }

  @ExceptionHandler(CustomParameterizedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ParameterizedErrorDto processParameterizedValidationError(
      CustomParameterizedException ex) {
    return ex.getErrorDto();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ErrorDto processAccessDeniedExcpetion(AccessDeniedException exception) {
    return new ErrorDto(ErrorConstants.ERR_ACCESS_DENIED, exception.getMessage());
  }

  private ErrorDto processFieldErrors(List<FieldError> fieldErrors) {
    ErrorDto dto = new ErrorDto(ErrorConstants.ERR_VALIDATION);

    for (FieldError fieldError : fieldErrors) {
      dto.add(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
    }

    return dto;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorDto processMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    return new ErrorDto(ErrorConstants.ERR_METHOD_NOT_SUPPORTED, exception.getMessage());
  }

  /**
   * Handler for wrapping exceptions which occur when an unparsable json is send by the client.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public JsonParsingError processHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    String errorMessage;
    if (exception.getRootCause() != null) {
      errorMessage = exception.getRootCause()
        .getLocalizedMessage();
    } else {
      errorMessage = exception.getLocalizedMessage();
    }
    return new JsonParsingError(errorMessage);
  }
}
