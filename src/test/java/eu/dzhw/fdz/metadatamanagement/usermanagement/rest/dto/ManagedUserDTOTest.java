/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.usermanagement.rest.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

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
