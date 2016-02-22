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
public class FilterExpressionLanguageTest {

  @Test
  public void testValueOf() {
    // Arrange
    FilterExpressionLanguage stata = FilterExpressionLanguage.valueOf("stata");

    // Act

    // Assert
    assertThat(stata.toString(), is("stata"));
  }

}
