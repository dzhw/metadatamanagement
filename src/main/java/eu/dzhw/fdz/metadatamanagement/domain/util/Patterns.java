package eu.dzhw.fdz.metadatamanagement.domain.util;

/**
 * Interface holding regexp for domain validation.
 * 
 * @author René Reitmann
 */
public interface Patterns {
  String ALPHANUMERIC_WITH_UNDERSCORE = "^[_A-Za-z0-9]*$";
  String GERMAN_ALPHANUMERIC_WITH_SPACE = "^[ A-Za-z0-9äöüÄÖÜ]*$";
}
