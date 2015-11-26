/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.errors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

/**
 * No Integration Test. No need for application Context.
 * 
 * @author Daniel Katzberg
 *
 */
public class ErrorDtoTest {

  @Test
  public void testErrorDto() {
    // Arrange
    ErrorDTO dto = new ErrorDTO("message");
    ErrorDTO dtoWithDescription = new ErrorDTO("message", "description");
    ErrorDTO dtoWithDescriptionAndFieldErrors =
        new ErrorDTO("message", "description", new ArrayList<>());

    // Act
    dto.add("objectName", "field", "messageAdd");

    // Assert
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));
    assertThat(dtoWithDescription.getDescription(), is("description"));
    assertThat(dto.getFieldErrors().size(), is(1));
    assertThat(dto.getFieldErrors().get(0).getMessage(), is("messageAdd"));
    assertThat(dto, not(nullValue()));
    assertThat(dtoWithDescription, not(nullValue()));
    assertThat(dtoWithDescriptionAndFieldErrors, not(nullValue()));
  }

}
