package eu.dzhw.fdz.metadatamanagement.common.domain.util;

/**
 * Interface holding regexp for domain validation.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public interface Patterns {
  String NUMERIC_WITH_DOT = "^[0-9][0-9.]*$";
  String ALPHANUMERIC_WITH_UNDERSCORE = "^[_A-Za-z0-9]*$";
  String ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN = "^[_A-Za-z][_A-Za-z0-9]*$";
  String GERMAN_ALPHANUMERIC = "^[A-Za-z0-9äöüÄÖÜß]*$";
  String GERMAN_ALPHANUMERIC_WITH_MINUS = "^[A-Za-z0-9äöüÄÖÜß\\-]*$";
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS = "^[_A-Za-z0-9äöüÄÖÜß\\-]*$";  
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT = "^[_A-Za-z0-9äöüÄÖÜß\\-\\.]*$";
  String NO_WHITESPACE = "^[^\\s]*$";
}
