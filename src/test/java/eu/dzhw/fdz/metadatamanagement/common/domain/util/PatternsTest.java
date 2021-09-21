/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.common.domain.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class PatternsTest {
  
  @Test
  public void testGermanAlphaNumericWithUnderscoreAndMinus() {
    //Arrange
    
    //Act    
    boolean valid = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "abc_123");
    boolean validUnderscoreAtEnd = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "-abc123_");
    boolean validUnderscoreAtFront = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "_abc123-");
    boolean validMinus = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "abc-123");
    boolean invalidSpace = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "abc 123");
    boolean validGermanUmlaut = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "äbc123");
    
    
    //Assert
    assertThat(valid, is(true));
    assertThat(validUnderscoreAtEnd, is(true));
    assertThat(validUnderscoreAtFront, is(true));
    assertThat(invalidSpace, is(false));
    assertThat(validMinus, is(true));
    assertThat(validGermanUmlaut, is(true));
  }

  @Test
  public void testAlphaNumericWithUnderscoreNoNumberAsFirstSign() {
    // Arrange

    // Act
    boolean valid =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "abc_123");
    boolean validUnderscoreAtEnd =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "abc123_");
    boolean validUnderscoreAtFront =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "_abc123");
    boolean invalidSpace =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "abc 123");
    boolean invalidGermanUmlaut =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "äbc123");
    boolean invalidNumberAsFirstSign =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "1abc_23");


    // Assert
    assertThat(valid, is(true));
    assertThat(validUnderscoreAtEnd, is(true));
    assertThat(validUnderscoreAtFront, is(true));
    assertThat(invalidSpace, is(false));
    assertThat(invalidGermanUmlaut, is(false));
    assertThat(invalidNumberAsFirstSign, is(false));
  }

}
