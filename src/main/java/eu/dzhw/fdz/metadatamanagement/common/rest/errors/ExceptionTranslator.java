package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.OptimisticLockingFailureException;
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
import com.mongodb.DuplicateKeyException;

import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyCreateNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopyDeleteNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.domain.ShadowCopySaveNotAllowedException;
import eu.dzhw.fdz.metadatamanagement.common.service.DuplicateFilenameException;
import freemarker.core.InvalidReferenceException;
import freemarker.core.ParseException;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * 
 * @author Daniel Katzberg
 */
@ControllerAdvice
public class ExceptionTranslator {

  private final MessageSourceAccessor messageSourceAccessor;

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

    ErrorListDto errorListDto = new ErrorListDto();

    // handle global errors
    for (ObjectError globalError : globalErrors) {
      errorListDto.add(
          new ErrorDto(globalError.getObjectName(), globalError.getDefaultMessage(), null, null));
    }

    // handle field errors
    for (FieldError fieldError : fieldErrors) {
      Object rejectedValue = fieldError.getRejectedValue();

      errorListDto.add(new ErrorDto(fieldError.getObjectName(), fieldError.getDefaultMessage(),
          rejectedValue, fieldError.getField()));
    }

    return errorListDto;
  }

  /**
   * Handle Freemarker parsing errors.
   */
  @ExceptionHandler(ParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorListDto processFreemarkerParseError(ParseException ex) {

    // Build ErrorDto
    String invalidValue =
        "(" + ex.getLineNumber() + "," + ex.getColumnNumber() + "): " + ex.getEditorMessage();
    ErrorDto errorDto = new ErrorDto(ex.getTemplateName(),
        "global.error.server-error.freemarker.parsing-error", invalidValue, null);

    // Build ErrorListDto
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }

  /**
   * Handle Freemarker invalid reference errors.
   */
  @ExceptionHandler(InvalidReferenceException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorListDto processFreemarkerParseError(InvalidReferenceException ex) {

    // Build ErrorDto
    String invalidValue = "(" + ex.getLineNumber() + "," + ex.getColumnNumber() + "): "
        + ex.getBlamedExpressionString();
    ErrorDto errorDto = new ErrorDto(ex.getTemplateSourceName(),
        "global.error.server-error.freemarker.invalid-reference-error", invalidValue, null);

    // Build ErrorListDto
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }


  /**
   * Handler for wrapping exceptions which occur when an unparsable json or excel file is send by
   * the client.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorListDto processHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {

    InvalidFormatException invalidFormatException =
        findInvalidFormatException(exception.getCause());

    JsonMappingException jsonMappingException = findJsonMappingException(exception.getCause());

    if (invalidFormatException != null) {
      return createParsingError(invalidFormatException);
      // Default message, if no other root cause was found
    } else if (jsonMappingException != null) {
      return createJsonMappingError(jsonMappingException);
    } else {
      String errorMessage;
      Throwable rootCause = exception.getRootCause();
      if (rootCause != null) {
        errorMessage = rootCause.getLocalizedMessage();
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

    // Get the Name of the Processor
    Class<?> processorClass = invalidFormatException.getProcessor().getClass();

    // Decide between different input types like excel or json files
    if (processorClass.equals(TreeTraversingParser.class)) {
      return this.createExcelParsingError(invalidFormatException);
    } else if (processorClass.equals(UTF8StreamJsonParser.class)) {
      return this.createJsonParsingError(invalidFormatException);
    } else {
      return new ErrorListDto();
    }
  }

  private ErrorListDto createExcelParsingError(InvalidFormatException invalidFormatException) {
    TreeTraversingParser processor = (TreeTraversingParser) invalidFormatException.getProcessor();

    // Create Excel Parsing Error. Just the first will be returned
    String domainObject =
        invalidFormatException.getPath().get(0).getFrom().getClass().getSimpleName();
    String property = processor.getCurrentName();
    if (property == null) {
      property = invalidFormatException.getPath().get(0).getFieldName();
    }
    String invalidValue = (String) invalidFormatException.getValue();
    String messageKey = "global.error.import.excel-parsing-error";
    return new ErrorListDto(new ErrorDto(domainObject, messageKey, invalidValue, property));
  }

  private ErrorListDto createJsonParsingError(InvalidFormatException invalidFormatException) {
    UTF8StreamJsonParser processor = (UTF8StreamJsonParser) invalidFormatException.getProcessor();

    // Create Json Parsing Error. Just the first will be returned
    try {
      String domainObject =
          invalidFormatException.getPath().get(0).getFrom().getClass().getSimpleName();

      String property = processor.getCurrentName();
      if (property == null) {
        property = invalidFormatException.getPath().get(0).getFieldName();
      }
      String invalidValue = (String) invalidFormatException.getValue();
      String messageKey = "global.error.import.json-parsing-error";
      return new ErrorListDto(new ErrorDto(domainObject, messageKey, invalidValue, property));
    } catch (IOException e) {
      return new ErrorListDto(
          new ErrorDto(null, "global.error.import.no-json-mapping", null, null));
    }
  }

  private ErrorListDto createJsonMappingError(JsonMappingException jsonMappingException) {

    String invalidField = jsonMappingException.getPath().stream().map(i -> i.getFieldName())
        .collect(Collectors.joining("."));

    return new ErrorListDto(
        new ErrorDto(null, "global.error.import.json-not-readable", invalidField, null));
  }

  /**
   * Handles {@link RepositoryConstraintViolationException}s by returning {@code 400 Bad Request}.
   * Introduces a custom dto for validation errors of spring data rest repositories. This is
   * necessary due to issue #706.
   * 
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

    for (FieldError fieldError : exception.getErrors().getFieldErrors()) {

      String message = this.messageSourceAccessor.getMessage(fieldError);

      errorListDto.add(new ErrorDto(fieldError.getObjectName(), message,
          fieldError.getRejectedValue(), fieldError.getField()));
    }

    return errorListDto;
  }

  /**
   * Handles {@link ConstraintViolationException}s by returning {@code 400 Bad Request}. Introduces
   * a custom dto for validation errors of spring data rest repositories. This is necessary due to
   * issue #706.
   * 
   * @param exception the exception to handle.
   * @return 400 bad request
   */
  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleConstraintViolationException(ConstraintViolationException exception) {

    ErrorListDto errorListDto = new ErrorListDto();

    for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
      String message = violation.getMessage();
      errorListDto.add(new ErrorDto(violation.getRootBeanClass().getSimpleName(), message,
          violation.getInvalidValue(),
          ((PathImpl) violation.getPropertyPath()).getLeafNode().getName()));
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
    } else {
      SizeLimitExceededException requestSizeException = findSizeLimitExceededException(exception);
      if (requestSizeException != null) {
        return new ErrorListDto(new ErrorDto("size", 
            "global.error.import.file-size-limit-exceeded",
            String.valueOf(requestSizeException.getActualSize()), null));
      }
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

  private SizeLimitExceededException findSizeLimitExceededException(Throwable exception) {
    if (exception == null || exception instanceof SizeLimitExceededException) {
      return (SizeLimitExceededException) exception;
    } else {
      return findSizeLimitExceededException(exception.getCause());
    }
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleDuplicateKeyException(DuplicateKeyException exception) {
    ErrorListDto errorListDto = new ErrorListDto();
    if (exception.getErrorMessage().contains("filename")) {
      ErrorDto error =
          new ErrorDto(null, "global.error.import.file-already-exists", null, "filename");
      errorListDto.add(error);
    } else {
      throw exception;
    }
    return errorListDto;
  }

  @ExceptionHandler
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleDuplicateFilenameException(DuplicateFilenameException exception) {
    ErrorListDto errorListDto = new ErrorListDto();
    ErrorDto error =
        new ErrorDto(null, "global.error.import.file-already-exists", null, "filename");
    errorListDto.add(error);
    return errorListDto;
  }

  /**
   * Handle {@link OptimisticLockingFailureException} thrown by attempts to save stale entities
   * through a repository. Responds to client with status 400.
   */
  @ExceptionHandler(OptimisticLockingFailureException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleOptimisticLockingFailureException() {
    ErrorDto errorDto = new ErrorDto(null, "global.error.optimistic-locking-failure", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }

  /**
   * Handle {@link ShadowCopySaveNotAllowedException} thrown by attempts to update a shadowed domain
   * object.
   */
  @ExceptionHandler(ShadowCopySaveNotAllowedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleShadowUpdateNotAllowedException() {
    ErrorDto errorDto = new ErrorDto(null, "global.error.shadow-save-not-allowed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }

  /**
   * Handle {@link ShadowCopyCreateNotAllowedException} thrown by attempts to create a shadowed
   * domain object.
   */
  @ExceptionHandler(ShadowCopyCreateNotAllowedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleShadowCreateNotAllowedException() {
    ErrorDto errorDto = new ErrorDto(null, "global.error.shadow-create-not-allowed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }

  /**
   * Handle {@link ShadowCopyDeleteNotAllowedException} thrown by attempts to delete a shadowed
   * domain object.
   */
  @ExceptionHandler(ShadowCopyDeleteNotAllowedException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ErrorListDto handleShadowDeleteNotAllowedException() {
    ErrorDto errorDto = new ErrorDto(null, "global.error.shadow-delete-not-allowed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }
}
