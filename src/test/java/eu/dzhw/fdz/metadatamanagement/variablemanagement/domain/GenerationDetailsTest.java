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
public class GenerationDetailsTest {

  @Test
  public void testGetterSetter() {
    // Arrange
    GenerationDetails generationDetails = UnitTestCreateDomainObjectUtils.buildGenerationDetails();

    // Act

    // Assert
    assertThat(generationDetails.getRule(), is("Rule 123 to 234"));
    assertThat(generationDetails.getRuleExpressionLanguage(), is(RuleExpressionLanguages.R));
    assertThat(generationDetails.getDescription()
      .getDe(), is("De Beschreibung"));
    assertThat(generationDetails.getDescription()
      .getEn(), is("En Description"));
  }

  @Test
  public void testToString() {

    // Arrange
    GenerationDetails generationDetails = UnitTestCreateDomainObjectUtils.buildGenerationDetails();

    // Act
    String toString = generationDetails.toString();

    // Assert
    assertThat(toString, is(
        "GenerationDetails{description=I18nString{de='De Beschreibung', en='En Description'}, rule=Rule 123 to 234, ruleExpressionLanguage=R}"));

  }

}
