/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.unittest.util.UnitTestCreateDomainObjectUtils;

/**
 * @author Daniel Katzberg
 *
 */
public class FilterDetailsTest {
  
  @Test
  public void testToString() {
    // Arrange
    FilterDetails filterDetails = UnitTestCreateDomainObjectUtils.buildFilterDetails();

    // Act
    String toString = filterDetails.toString();

    // Assert
    assertThat(toString, is(
        "FilterDetails{filterExpression=A Filter Expression, filterDescription=I18nString{de='Eine Filterbeschreibung.', en='A filter description.'}, filterExpressionLanguage=stata}"));

  }

}
