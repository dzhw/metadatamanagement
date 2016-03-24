/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * @author Daniel Katzberg
 *
 */
public class PatternsTest {
  
  @Test
  public void testNumericWithDot() {
    //Arrange
    
    //Act
    boolean valid = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "123.00");
    boolean validDotAtEnd = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "12300.");
    boolean validDotAtFront = Pattern.matches(Patterns.NUMERIC_WITH_DOT, ".12300");
    boolean invalidSpace = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "123 .00");
    boolean invalidSign = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "123,00");
    boolean invalidLetter = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "123.aa");
    boolean invalidGermanUmlaut = Pattern.matches(Patterns.NUMERIC_WITH_DOT, "123.ää");
    
    //Assert
    assertThat(valid, is(true));
    assertThat(validDotAtEnd, is(true));
    assertThat(validDotAtFront, is(true));
    assertThat(invalidSpace, is(false));
    assertThat(invalidSign, is(false));
    assertThat(invalidLetter, is(false));
    assertThat(invalidGermanUmlaut, is(false));
  }
  
  
  @Test
  public void testAlphaNumericWithUnderscore() {
    //Arrange
    
    //Act
    boolean valid = Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE, "Abc_123");
    boolean validUnderscoreAtEnd = Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE, "Abc123_");
    boolean validUnderscoreAtFront = Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE, "_Abc123");
    boolean invalidSpace = Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE, " Abc_123");
    boolean invalidGermanUmlaut = Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE, "Äbc_123");
    
    
    //Assert
    assertThat(valid, is(true));
    assertThat(validUnderscoreAtEnd, is(true));
    assertThat(validUnderscoreAtFront, is(true));
    assertThat(invalidSpace, is(false));
    assertThat(invalidGermanUmlaut, is(false));
  }
  
  @Test
  public void testGermanAlphaNumericWithSpace() {
    //Arrange
    
    //Act    
    boolean valid = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE, "Abc 123");
    boolean validSpaceAtEnd = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE, "Abc123 ");
    boolean validSpaceAtFront = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE, " Abc123");
    boolean invalidUnderscore = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE, "Abc_123");
    boolean validGermanUmlaut = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_SPACE, "Äbc 123");
    
    
    //Assert
    assertThat(valid, is(true));
    assertThat(validSpaceAtEnd, is(true));
    assertThat(validSpaceAtFront, is(true));
    assertThat(invalidUnderscore, is(false));
    assertThat(validGermanUmlaut, is(true));
  }
  
  @Test
  public void testGermanAlphaNumericWithUnderscoreAndMinus() {
    //Arrange
    
    //Act    
    boolean valid = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "Abc_123");
    boolean validUnderscoreAtEnd = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "-Abc123_");
    boolean validUnderscoreAtFront = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "_Abc123-");
    boolean validMinus = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "Abc-123");
    boolean invalidSpace = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "Abc 123");
    boolean validGermanUmlaut = Pattern.matches(Patterns.GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS, "Äbc123");
    
    
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
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "Abc_123");
    boolean validUnderscoreAtEnd =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "Abc123_");
    boolean validUnderscoreAtFront =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "_Abc123");
    boolean invalidSpace =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "Abc 123");
    boolean invalidGermanUmlaut =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "Äbc123");
    boolean invalidNumberAsFirstSign =
        Pattern.matches(Patterns.ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN, "1Abc_23");


    // Assert
    assertThat(valid, is(true));
    assertThat(validUnderscoreAtEnd, is(true));
    assertThat(validUnderscoreAtFront, is(true));
    assertThat(invalidSpace, is(false));
    assertThat(invalidGermanUmlaut, is(false));
    assertThat(invalidNumberAsFirstSign, is(false));
  }

}
