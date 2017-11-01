/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * @author Daniel Katzberg
 *
 */
public class VariableTest {

  @Test
  public void testEquals() {

    // Arrange
    Variable variable1 = Variable.builder().id("1")
      .build();
    Variable variable1_1 = Variable.builder().id("1")
      .label(I18nString.builder().de("Label").en("Label").build())
      .build();
    Variable variable2 = Variable.builder().id("2")
      .build();

    // Act
    boolean checkNull = variable1.equals(null);
    boolean checkClass = variable1.equals(new Object());
    boolean checkSame = variable1.equals(variable1);
    boolean checkDifferent = variable1.equals(variable2);
    boolean checkSameIdButDifferent = variable1.equals(variable1_1);

    // Assert
    assertThat(checkNull, is(false));
    assertThat(checkClass, is(false));
    assertThat(checkSame, is(true));
    assertThat(checkDifferent, is(false));
    assertThat(checkSameIdButDifferent, is(true));

  }

}
