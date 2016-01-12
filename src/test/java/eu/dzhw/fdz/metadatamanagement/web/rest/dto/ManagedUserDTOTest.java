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
public class ManagedUserDTOTest {

  @Test
  public void testManagedDTOLastModified() {
    // Arrange
    ManagedUserDto dto = new ManagedUserDto();

    // Act
    dto.setLastModifiedBy("last Modified");

    // Assert
    assertThat(dto.getLastModifiedBy(), is("last Modified"));
  }

}
