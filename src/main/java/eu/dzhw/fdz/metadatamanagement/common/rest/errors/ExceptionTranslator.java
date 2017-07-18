package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;

import com.fasterxml.jackson.core.json.UTF8StreamJsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.exception.TemplateIncompleteException;
import freemarker.template.TemplateException;

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

  /**
   * Handle validation errors.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorListDto processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<ObjectError> globalErrors = result.getGlobalErrors();
    List<FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(globalErrors, fieldErrors);
  }
  
  private ErrorListDto processFieldErrors(List<ObjectError> globalErrors, 
      List<FieldError> fieldErrors) {
    
    ErrorListDto errorListDto =  new ErrorListDto();

    //handle global errors
    for (ObjectError globalError: globalErrors) {
      errorListDto.add(new ErrorDto(globalError.getObjectName(), 
          globalError.getDefaultMessage(), null, null));
    }
    
    //handle field errors
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

  /**
   * Handler for wrapping exceptions which occur when an unparsable json or excel file 
   * is send by the client.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorListDto processHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    
    InvalidFormatException invalidFormatException =  
        findInvalidFormatException(exception.getCause());
    
    JsonMappingException jsonMappingException =
        findJsonMappingException(exception.getCause());
    
    if (invalidFormatException != null) {
      return createParsingError(invalidFormatException);
    //Default message, if no other root cause was found  
    } else if (jsonMappingException != null) {      
      return createJsonMappingError(jsonMappingException);
    } else {
      String errorMessage;      
      if (exception.getRootCause() != null) {
        errorMessage = exception.getRootCause()
          .getLocalizedMessage();
      } else {
        errorMessage = exception.getLocalizedMessage();
      }
      return new ErrorListDto(new ErrorDto(null, errorMessage, null, null));
      
    }
  }
  
  private InvalidFormatException findInvalidFormatException(Throwable cause) {
    if (cause == null || cause instanceof InvalidFormatException) {
      return (InvalidFormatException) cause;
    } else {
      return findInvalidFormatException(cause.getCause());
    }
  }
  
  private JsonMappingException findJsonMappingException(Throwable cause) {
    if (cause == null || cause instanceof JsonMappingException) {
      return (JsonMappingException) cause;
    } else {
      return findJsonMappingException(cause.getCause());
    }
  }
  
  private ErrorListDto createParsingError(InvalidFormatException invalidFormatException) { 
    
    //Get the Name of the Processor
    String nameOfProcessor = invalidFormatException.getProcessor().getClass().getSimpleName();
    
    //Decide between different input types like excel or json files
    switch (nameOfProcessor) {
      //Excel Parsing Error
      case "TreeTraversingParser":
        return this.createExcelParsingError(invalidFormatException);
      //Json Parsing Error
      case "UTF8StreamJsonParser":
        return this.createJsonParsingError(invalidFormatException);
      default:
        return new ErrorListDto();
    }
  }
  
  private ErrorListDto createExcelParsingError(InvalidFormatException invalidFormatException) {
    TreeTraversingParser processor = 
        (TreeTraversingParser) invalidFormatException.getProcessor();  
    
    //Create Excel Parsing Error. Just the first will be returned                     
    String domainObject = invalidFormatException.getPath().get(0).getFrom()
        .getClass().getSimpleName();
    String property = processor.getCurrentName();
    if (property == null) {
      property = invalidFormatException.getPath().get(0).getFieldName();
    }
    String invalidValue = (String)invalidFormatException.getValue();
    String messageKey = "global.error.import.excel-parsing-error";
    return new ErrorListDto(new ErrorDto(domainObject, messageKey,invalidValue, property));    
  }
  
  private ErrorListDto createJsonParsingError(InvalidFormatException invalidFormatException) {
    UTF8StreamJsonParser processor = 
        (UTF8StreamJsonParser) invalidFormatException.getProcessor();  
    
    //Create Json Parsing Error. Just the first will be returned
    try {            
      
      String domainObject = invalidFormatException.getPath().get(0).getFrom()
          .getClass().getSimpleName();
      String property = processor.getCurrentName();
      if (property == null) {
        property = invalidFormatException.getPath().get(0).getFieldName();
      }
      String invalidValue = (String)invalidFormatException.getValue();
      String messageKey = "global.error.import.json-parsing-error";
      return new ErrorListDto(new ErrorDto(domainObject, messageKey,invalidValue, property));
    } catch (IOException e) {
      return new ErrorListDto(
          new ErrorDto(null, "global.error.import.no-json-mapping", null, null));
    }
  }
  
  private ErrorListDto createJsonMappingError(JsonMappingException jsonMappingException) {
   
    String invalidField = jsonMappingException.getPath()
        .stream()
        .map(i -> i.getFieldName())
        .collect(Collectors.joining("."));
    
    return new ErrorListDto(
        new ErrorDto(null, "global.error.import.json-not-readable", invalidField, null));
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
  ErrorListDto handleRepositoryConstraintViolationException(
      RepositoryConstraintViolationException exception) {
    
    ErrorListDto errorListDto = new ErrorListDto();
    
    for (ObjectError globalError : exception.getErrors().getGlobalErrors()) {
      String message = this.messageSourceAccessor.getMessage(globalError);
      errorListDto.add(new ErrorDto(globalError.getObjectName(), message, null, null));
    }
    
    for (FieldError fieldError : exception.getErrors()
        .getFieldErrors()) {

      String message = this.messageSourceAccessor.getMessage(fieldError);

      errorListDto.add(new ErrorDto(fieldError.getObjectName(), message,
          String.format("%s", fieldError.getRejectedValue()), fieldError.getField()));
    }
    
    return errorListDto;
  }
  
  /**
   * Handles the Dataset Report Template exception. This exception will be thrown, 
   * if something gone wrong on creating latex code by the given freemarker code.
   * @param exception The exception by the template handling.
   * @return 400 Bad Request and the error message.
   */
  @ExceptionHandler(TemplateException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleTemplateException(TemplateException exception) {
    
    //The message of the exception is the error message of freemarker.
    //The manually added message for the dto can be translated into i18n strings
    String messageKey = "data-set-management.error.tex-template-error";
    ErrorListDto errorListDto = new ErrorListDto(
        new ErrorDto(null, messageKey, exception.getMessage(), null));
    
    return errorListDto;
  }
  
  /**
   * Handles the Dataset Report Template incomplete exception. This exception will be thrown, 
   * if the uploaded tex template files are incomplete and files are missing.
   * @param exception The incomplete tex template exception to handle.
   * @return 400 Bad Request and a list of missing files within the exception.
   */
  @ExceptionHandler(TemplateIncompleteException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleIncompleteTemplateException(TemplateIncompleteException exception) {
    ErrorListDto errorListDto = new ErrorListDto();
    
    //All missing files
    for (String missingFile : exception.getMissingFiles()) {
      errorListDto.add(new ErrorDto(null, exception.getMessage(), missingFile, null));
    }
    
    return errorListDto;
  }
  
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleMultipartException(MultipartException exception) {
    FileSizeLimitExceededException fileSizeException = 
        findFileSizeLimitExceededException(exception);
    if (fileSizeException != null) {
      return new ErrorListDto(new ErrorDto(fileSizeException.getFileName(), 
          "global.error.import.file-size-limit-exceeded",
          String.valueOf(fileSizeException.getActualSize()), null));
    }
    // return the message as it is 
    return new ErrorListDto(new ErrorDto(null, exception.getLocalizedMessage(), null, null));
  }
  
  private FileSizeLimitExceededException findFileSizeLimitExceededException(Throwable exception) {
    if (exception == null || exception instanceof FileSizeLimitExceededException) {
      return (FileSizeLimitExceededException) exception;
    } else {
      return findFileSizeLimitExceededException(exception.getCause());
    }
  }
}
