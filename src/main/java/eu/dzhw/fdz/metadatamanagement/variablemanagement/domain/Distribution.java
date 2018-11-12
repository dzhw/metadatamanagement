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
 * A distribution contains the descriptives of a {@link Variable} meaning its
 * {@link ValidResponse}s, {@link Missing}s and {@link Statistics}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Distribution {

  /**
   * The total absolute number of {@link ValidResponse}s and {@link Missing}s.
   * 
   * Must not be empty.
   */
  @NotNull(message =
      "variable-management.error.distribution.total-absolute-frequency.not-null")
  private Integer totalAbsoluteFrequency;

  /**
   * The total absolute number of {@link ValidResponse}s.
   * 
   * Must not be empty.
   */
  @NotNull(message =
      "variable-management.error.distribution.total-valid-absolute-frequency.not-null")
  private Integer totalValidAbsoluteFrequency;

  /**
   * The quotient from totalValidAbsoluteFrequency and totalAbsoluteFrequency.
   * 
   * Must not be empty.
   */
  @NotNull(message =
      "variable-management.error.distribution.total-valid-relative-frequency.not-null")
  private Double totalValidRelativeFrequency;

  /**
   * Descriptive metrics of this {@link Variable}.
   */
  @Valid
  private Statistics statistics;

  /**
   * List of {@link Missing}s of this {@link Variable}.
   * 
   * Must not contain more than 7000 entries and the code of the {@link Missing}s must be unique.
   */
  @UniqueCode(message =
      "variable-management.error.distribution.missings.uniqueCode")
  @Valid
  @Size(max = 7000, message = "variable-management.error.distribution.missings.max-size")
  private List<Missing> missings;

  /**
   * List of {@link ValidResponse}s of this variable.
   * 
   * Must not contain more than 7000 entries and the value of the {@link ValidResponse}s must be
   * unique.
   */
  @UniqueValue(message =
      "variable-management.error.distribution.valid-responses.unique-value")
  @Valid
  @Size(max = 7000, message = "variable-management.error.distribution.valid-responses.max-size")
  private List<ValidResponse> validResponses;

  /**
   * Integer used for rounding the values of this {@link Variable} when displaying it. It is
   * computed during the import of the {@link Variable} by finding the maximum number of decimal
   * places in the list of {@link ValidResponse}s.
   */
  private Integer maxNumberOfDecimalPlaces;
}
