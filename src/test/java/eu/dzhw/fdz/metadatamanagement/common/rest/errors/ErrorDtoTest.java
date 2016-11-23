/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.rest.errors;

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
public class ErrorDtoTest {

  @Test
  public void testErrorDto() {
    // Arrange
    ErrorDto dto = new ErrorDto(null, "message", null, null);
    ErrorDto dtoWithEntity = new ErrorDto("entityId", "message", null, null);
    ErrorDto dtoWithDescriptionAndInvalidError =
        new ErrorDto("entityId", "message", "InvalidValue", null);
    ErrorDto dtoWithDescriptionAndInvalidErrorAndProperty =
        new ErrorDto("entityId", "message", "InvalidValue", "Property");

    // Act

    // Assert
    assertThat(dto, not(nullValue()));
    assertThat(dto.getMessage(), is("message"));    
    assertThat(dtoWithEntity, not(nullValue()));
    assertThat(dtoWithEntity.getEntity(), is("entityId"));
    assertThat(dtoWithDescriptionAndInvalidError, not(nullValue()));
    assertThat(dtoWithDescriptionAndInvalidError.getInvalidValue(), is("InvalidValue"));
    assertThat(dtoWithDescriptionAndInvalidErrorAndProperty, not(nullValue()));
    assertThat(dtoWithDescriptionAndInvalidErrorAndProperty.getProperty(), is("Property"));
  }

}
