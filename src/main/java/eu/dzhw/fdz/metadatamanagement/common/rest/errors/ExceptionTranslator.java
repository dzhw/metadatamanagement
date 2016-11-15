package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
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

import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {

  private MessageSourceAccessor messageSourceAccessor;
  
  @Autowired
  public ExceptionTranslator(MessageSource messageSource) {
    this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
  }
  
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
  //TODO DKatzberg: Clean up, upgrade
  public JsonParsingError processHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    
    Throwable throwable = exception.getMostSpecificCause();
    if (throwable instanceof InvalidFormatException) {  
      InvalidFormatException jsonMappingException = ((InvalidFormatException) throwable);
      UTF8StreamJsonParser processor = 
          (UTF8StreamJsonParser) jsonMappingException.getProcessor();      
      
      try {
                       
        String domainObject = processor.getCurrentValue().getClass().getSimpleName()
            .toLowerCase(LocaleContextHolder.getLocale());
        String property = processor.getCurrentName();
//        String invalidValue = (String)jsonMappingException.getValue();
        String messageKey = domainObject + "-management.error.import." 
            + domainObject + "." + property;
        
//        return new CustomRepositoryConstraintViolationExceptionMessage
//        .ValidationError(domainObject, messageKey, invalidValue, property);
        return new JsonParsingError(messageKey);
        
        
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      return new JsonParsingError("");
      
    } else {
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
  
  /**
   * Handles {@link RepositoryConstraintViolationException}s by returning {@code 400 Bad Request}.
   * Introduces a custom dto for validation errors of spring data rest repositories.
   * This is necessary due to issue #706. 
   * @param exception the exception to handle.
   * @return 400 bad request
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  CustomRepositoryConstraintViolationExceptionMessage handleRepositoryConstraintViolationException(
          RepositoryConstraintViolationException exception) {
    return new CustomRepositoryConstraintViolationExceptionMessage(
        exception, messageSourceAccessor);
  }
}
