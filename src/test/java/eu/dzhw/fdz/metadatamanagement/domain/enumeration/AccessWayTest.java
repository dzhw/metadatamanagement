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
public class AccessWayTest {

  @Test
  public void testValueOf() {
    // Arrange
    AccessWay remote = AccessWay.valueOf("remote");

    // Act

    // Assert
    assertThat(remote.toString(), is("remote"));
  }
}
