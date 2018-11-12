package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.time.LocalDate;

import org.javers.core.metamodel.annotation.ValueObject;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objects representing periods in time. All periods must have a start date and an end date and the
 * start date must be before or equal to the end date.
 */
@ValidPeriod(message = "global.error.period.valid-period")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@ValueObject
public class Period {
  /**
   * The start date of the period.
   * 
   * Mandatory and must not be after end date.
   */
  private LocalDate start;

  /**
   * The end date of the period.
   * 
   * Mandatory and must not be before start date.
   */
  private LocalDate end;
}
