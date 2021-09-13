/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import eu.dzhw.fdz.metadatamanagement.common.rest.dto.LoggerDto;

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
