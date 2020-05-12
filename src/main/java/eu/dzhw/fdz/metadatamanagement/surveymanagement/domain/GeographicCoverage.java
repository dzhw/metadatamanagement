package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import java.io.Serializable;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidCountryCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains data regarding the location where survey data was collected.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GeographicCoverage implements Serializable {

  private static final long serialVersionUID = -1832250913323007347L;

  /**
   * ISO 3166-1 alpha-2 country code.
   */
  @ValidCountryCode(message = "survey-management.error.geographic-coverage.country."
      + "valid-country-code")
  private String country;

  /**
   * Free text description for additional information regarding the location. Markdown is supported.
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.geographic-coverage.description.i18n-string-size")
  private I18nString description;
}
