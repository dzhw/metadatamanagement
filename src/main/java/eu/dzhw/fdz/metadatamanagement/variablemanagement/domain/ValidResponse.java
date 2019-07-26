
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A valid response represents one observation of a {@link Variable} and its frequency.
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class ValidResponse implements Serializable {
  
  private static final long serialVersionUID = -5291769315828068076L;

  /**
   * An optional label for the value of this observation.
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.valid-response.label.i18n-string-size")
  private I18nString label;

  /**
   * The absolute number of occurrences of this observation.
   * 
   * Must not be empty.
   */
  @NotNull(
      message = "variable-management.error.valid-response.absolute-frequency.not-null")
  private Integer absoluteFrequency;

  /**
   * The quotient from absoluteFrequency and {@link Distribution}.totalAbsoluteFrequency.
   * 
   * Must not be empty.
   */
  @NotNull(
      message = "variable-management.error.valid-response.relative-frequency.not-null")
  private Double relativeFrequency;

  /**
   * The value which has been observed (e.g. was responded by the participant).
   * 
   * Must not be empty and must not contain more than 256 characters.
   */
  @NotEmpty(message = "variable-management.error.valid-response.value.not-null")
  @Size(max = 256,
      message = "variable-management.error.valid-response.value.size")
  private String value;

  /**
   * The quotient from absoluteFrequency and {@link Distribution}.totalValidAbsoluteFrequency.
   * 
   * Must not be empty.
   */
  @NotNull(
      message = "variable-management.error.valid-response.validRelativeFrequency.not-null")
  private Double validRelativeFrequency;
}
