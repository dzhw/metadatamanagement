package eu.dzhw.fdz.metadatamanagement.domain.enumeration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class RuleExpressionLanguageTest {

  @Test
  public void testValueOf() {
    // Arrange
    RuleExpressionLanguage stata = RuleExpressionLanguage.valueOf("Stata");

    // Act

    // Assert
    assertThat(stata.toString(), is("Stata"));
  }

}
