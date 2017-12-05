package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringNotEmpty;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The representation of the population for the survey.
 * 
 * @author Daniel Katzberg
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Population {
  
  @NotNull(message = "survey-management.error.population.title.not-null")  
  @I18nStringNotEmpty(message = "survey-management.error.population.title.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "survey-management.error.population.title.i18n-string-size")
  private I18nString title;
  
  @NotNull(message = "survey-management.error.population.description.not-null")  
  @I18nStringNotEmpty(
      message = "survey-management.error.population.description.i18n-string-not-empty")
  @I18nStringSize(max = StringLengths.LARGE,
      message = "survey-management.error.population.description.i18n-string-size")
  private I18nString description;

}
