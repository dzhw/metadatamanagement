package eu.dzhw.fdz.metadatamanagement.common.domain.util;

/**
 * Interface holding regexp for domain validation.
 * 
 * @author René Reitmann
 * @author Daniel Katzberg
 */
public interface Patterns {
  String ALPHANUMERIC_WITH_UNDERSCORE_NO_NUMBER_AS_FIRST_SIGN = "^[_a-zA-Z][_a-zA-Z0-9]*$";
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS = "^[_A-Za-z0-9äöüÄÖÜß\\-]*$";
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOLLAR  = 
      "^[_A-Za-z0-9äöüÄÖÜß\\-\\$]*$";
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT = "^[_A-Za-z0-9äöüÄÖÜß\\-\\.]*$";
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT_AND_DOLLAR =
      "^[_A-Za-z0-9äöüÄÖÜß\\-\\.\\$]*$";
  String NO_WHITESPACE = "^[^\\s]*$";
  
  String SEMVER = "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)"
      + "(-(0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)"
      + "(\\.(0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*)?(\\+[0-9a-zA-Z-]+(\\.[0-9a-zA-Z-]+)*)?$";
}
