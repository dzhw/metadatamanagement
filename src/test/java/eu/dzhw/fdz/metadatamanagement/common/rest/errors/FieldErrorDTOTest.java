/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.FieldErrorDto;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class FieldErrorDTOTest {

  @Test
  public void testFieldErrorDTOTest() {
    // Arrange
    FieldErrorDto dto = new FieldErrorDto("dto", "field", "message");

    // Act

    // Assert
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));
    assertThat(dto.getObjectName(), is("dto"));
    assertThat(dto.getField(), is("field"));
  }
}
