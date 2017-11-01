/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.unittesthelper.util.UnitTestCreateDomainObjectUtils;

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
        "FilterDetails(expression=A Filter Expression, description=I18nString(de=Eine Filterbeschreibung., en=A filter description.), expressionLanguage=Stata)"));

  }

}
