package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * 
 * @author Daniel Katzberg
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
    //TODO DKatzberg replaxe the test key with a real key
    return new ErrorDto(null, "error.concurrencyError.DEMO.test", null, null);
  }

  /**
   * Handle validation errors.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  //TODO DKatzberg replace with the new dtos
  public ErrorListDto processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<ObjectError> globalErrors = result.getGlobalErrors();
    List<FieldError> fieldErrors = result.getFieldErrors();

    return processFieldErrors(globalErrors, fieldErrors);
  }

  @ExceptionHandler(CustomParameterizedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorDto processParameterizedValidationError(CustomParameterizedException ex) {
    return ex.getErrorDto();
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public ErrorDto processAccessDeniedExcpetion(AccessDeniedException exception) {
    //TODO DKatzberg other field? orther message? Change later
    return new ErrorDto(null, "error.AccessDenied.DEMO.test", null, null);
  }

  private ErrorListDto processFieldErrors(List<ObjectError> globalErrors, 
      List<FieldError> fieldErrors) {
    //TODO DKatzberg replace the test key with a real key
    ErrorListDto errorListDto =  new ErrorListDto();
    for (ObjectError globalError: globalErrors) {
      //TODO DKatzberg possibly add more elements?
      errorListDto.add(new ErrorDto(globalError.getObjectName(), 
          globalError.getDefaultMessage(), null, null));
    }
    
    for (FieldError fieldError : fieldErrors) {
      String rejectedValue = null;
      
      if (fieldError.getRejectedValue() != null) {
        rejectedValue = fieldError.getRejectedValue().toString();
      }
      
      errorListDto.add(new ErrorDto(fieldError.getObjectName(), fieldError.getDefaultMessage(), 
          rejectedValue, fieldError.getField()));
    }

    return errorListDto;
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  public ErrorDto processMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    //TODO DKatzberg: Check the Message, ggf other fields.
    return new ErrorDto(null, "error.MethodNotSupported.DEMO.test", null, null);
  }

  /**
   * Handler for wrapping exceptions which occur when an unparsable json is send by the client.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorListDto processHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    InvalidFormatException invalidFormatException =  
        findInvalidFormatException(exception.getCause());
    if (invalidFormatException != null) {  
      return createJsonParsingError(invalidFormatException);
    } else {
      String errorMessage;
      if (exception.getRootCause() != null) {
        errorMessage = exception.getRootCause()
          .getLocalizedMessage();
      } else {
        errorMessage = exception.getLocalizedMessage();
      }
      return new ErrorListDto(errorMessage, null, null, null);
    }
  }
  
  private InvalidFormatException findInvalidFormatException(Throwable cause) {
    if (cause == null || cause instanceof InvalidFormatException) {
      return (InvalidFormatException) cause;
    } else {
      return findInvalidFormatException(cause.getCause());
    }
  }
  
  private ErrorListDto createJsonParsingError(InvalidFormatException invalidFormatException) {
    UTF8StreamJsonParser processor = 
        (UTF8StreamJsonParser) invalidFormatException.getProcessor();      
    
    //Create Json Parsing Error. Just the first will be returned
    try {            
      String domainObject = processor.getCurrentValue().getClass().getSimpleName();
      String property = processor.getCurrentName();
      String invalidValue = (String)invalidFormatException.getValue();
      String messageKey = "global.error.import.json-parsing-error";
      return new ErrorListDto(messageKey, domainObject, invalidValue, property);
    } catch (IOException e) {
      return new ErrorListDto("global.error.import.no-json-mapping", null, null, null);
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
  
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleMultipartException(MultipartException exception) {
    FileSizeLimitExceededException fileSizeException = 
        findFileSizeLimitExceededException(exception);
    if (fileSizeException != null) {
      return new ErrorListDto("global.error.import.file-size-limit-exceeded", 
          fileSizeException.getFileName(), 
          String.valueOf(fileSizeException.getActualSize()), null);
    }
    // return the message as it is 
    return new ErrorListDto(exception.getLocalizedMessage(), null, null, null);
  }
  
  private FileSizeLimitExceededException findFileSizeLimitExceededException(Throwable exception) {
    if (exception == null || exception instanceof FileSizeLimitExceededException) {
      return (FileSizeLimitExceededException) exception;
    } else {
      return findFileSizeLimitExceededException(exception.getCause());
    }
  }
}
