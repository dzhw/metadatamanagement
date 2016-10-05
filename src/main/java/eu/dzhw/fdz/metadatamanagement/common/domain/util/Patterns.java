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
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS = "^[_A-Za-z0-9äöüÄÖÜß\\-]*$";  
  String GERMAN_ALPHANUMERIC_WITH_UNDERSCORE_AND_MINUS_AND_DOT = "^[_A-Za-z0-9äöüÄÖÜß\\-\\.]*$";
  
  //The Pattern ist from the URL: https://mathiasbynens.be/demo/url-regex and from @diegoperini 
  //The Pattern was aftwards escaped special signs for java.
  String VALID_URL = "_^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!10(?:\\.\\d{1,3}){3})(?!127"
      + "(?:\\.\\d{1,3}){3})(?!169\\.254(?:\\.\\d{1,3}){2})(?!192\\.168(?:\\.\\d{1,3}){2})(?!172\\."
      + "(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?"
      + "\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z"
      + "\\x{00a1}-\\x{ffff}0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}"
      + "0-9]+-?)*[a-z\\x{00a1}-\\x{ffff}0-9]+)*(?:\\.(?:[a-z\\x{00a1}-\\x{ffff}]{2,})))(?::\\d{2,5"
      + "})?(?:/[^\\s]*)?$_iuS";
}
