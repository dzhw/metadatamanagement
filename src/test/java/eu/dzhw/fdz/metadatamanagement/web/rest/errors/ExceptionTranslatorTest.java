/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class ExceptionTranslatorTest {

  @Test
  public void testProcessConcurencyError() {
    // Arrange
    ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    // Act
    ErrorDTO dto =
        exceptionTranslator.processConcurencyError(new ConcurrencyFailureException("message"));

    // Assert
    assertThat(exceptionTranslator, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("error.concurrencyFailure"));
  }

  @Test
  public void testProcessValidationError() {
    // Arrange
    ExceptionTranslator exceptionTranslator = new ExceptionTranslator();
    BindingResult bindingResult = Mockito.mock(BindingResult.class);
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.add(new FieldError("objectName", "field", "message"));
    when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
    
    // Act
    ErrorDTO dto = exceptionTranslator.processValidationError(new MethodArgumentNotValidException(
        Mockito.mock(MethodParameter.class), bindingResult));

    // Assert
    assertThat(exceptionTranslator, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("error.validation"));
  }

  @Test
  public void testProcessParameterizedValidationError() {
    // Arrange
    ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    // Act
    ParameterizedErrorDTO dto = exceptionTranslator
      .processParameterizedValidationError(new CustomParameterizedException("message"));

    // Assert
    assertThat(exceptionTranslator, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));
  }

  @Test
  public void testProcessAccessDeniedExcpetion() {
    // Arrange
    ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    // Act
    ErrorDTO dto =
        exceptionTranslator.processAccessDeniedExcpetion(new AccessDeniedException("message"));

    // Assert
    assertThat(exceptionTranslator, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("error.accessDenied"));
  }

  @Test
  public void testProcessMethodNotSupportedException() {
    // Arrange
    ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    // Act
    ErrorDTO dto = exceptionTranslator
      .processMethodNotSupportedException(new HttpRequestMethodNotSupportedException("method", "message"));

    // Assert
    assertThat(exceptionTranslator, not(nullValue()));
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("error.methodNotSupported"));
  }

}
