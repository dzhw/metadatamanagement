/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
    FieldErrorDTO dto = new FieldErrorDTO("dto", "field", "message");

    // Act

    // Assert
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));
    assertThat(dto.getObjectName(), is("dto"));
    assertThat(dto.getField(), is("field"));
  }
}
