/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.web.rest.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg No Integration Test. No need for application Context.
 */
public class LoggerDTOTest {

  @Test
  public void testToString() {
    // Arrange
    LoggerDto dto = new LoggerDto();

    // Act

    // Assert
    assertThat(dto.toString(), is("LoggerDTO{name='null', level='null'}"));
  }

}
