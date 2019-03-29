package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a country with it's 2-letter country code and it's display name in
 * german and english.
 */
@Data
@AllArgsConstructor
public class Country {
  private String code;
  private String de;
  private String en;
}
