package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringEntireNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation.ValidUnitValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javers.core.metamodel.annotation.ValueObject;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Details of the population of a {@link Survey}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class Population {
  /**
   * A description of the population.
   * It must be specified in all languages and it must not contain more than 2048
   * characters.
   */
  @NotNull(message = "survey-management.error.population.description.not-null")
  @I18nStringEntireNotEmpty(
      message = "survey-management.error.population.description.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.population.description.i18n-string-size")
  private I18nString description;

  /**
   * A list of geographic coverages. Must contain at least one entry.
   */
  @Valid
  @NotEmpty(message = "survey-management.error.population.geographic-coverages.not-empty")
  private List<GeographicCoverage> geographicCoverages;

  /**
   * Unit type. Mandatory field which only allows values specified by VFDB.
   * @see <a href="https://mdr.iqb.hu-berlin.de/#/catalog/94d1ae4f-a441-c728-4a03-adb0eb4604af">
   * GNERD: Survey Unit Educational Research (Version 1.0)
   * </a>
   */
  @NotNull(message = "survey-management.error.population.unit.not-null")
  @ValidUnitValue(message = "survey-management.error.population.valid-unit-value")
  private I18nString unit;
}
