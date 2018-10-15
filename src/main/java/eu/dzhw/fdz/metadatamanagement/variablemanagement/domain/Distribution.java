package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueCode;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.UniqueValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain object of the ValueSummary. Represent sums for all values of the variable.
 *
 * @author Daniel Katzberg
 *
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Distribution {

  @NotNull(message =
      "variable-management.error.distribution.total-absolute-frequency.not-null")
  private Integer totalAbsoluteFrequency;

  @NotNull(message =
      "variable-management.error.distribution.total-valid-absolute-frequency.not-null")
  private Integer totalValidAbsoluteFrequency;

  @NotNull(message =
      "variable-management.error.distribution.total-valid-relative-frequency.not-null")
  private Double totalValidRelativeFrequency;

  // No validation
  @Valid
  private Statistics statistics;

  @UniqueCode(message =
      "variable-management.error.distribution.missings.uniqueCode")
  @Valid
  @Size(max = 7000, message = "variable-management.error.distribution.missings.max-size")
  private List<Missing> missings;

  @UniqueValue(message =
      "variable-management.error.distribution.valid-responses.unique-value")
  @Valid
  @Size(max = 7000, message = "variable-management.error.distribution.valid-responses.max-size")
  private List<ValidResponse> validResponses;

  private Integer maxNumberOfDecimalPlaces;
}
