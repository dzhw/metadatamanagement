/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.enumeration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelTest {

  @Test
  public void testValueOf() {
    // Arrange
    ScaleLevel nominal = ScaleLevel.valueOf("nominal");

    // Act

    // Assert
    assertThat(nominal.toString(), is("nominal"));
  }

}
