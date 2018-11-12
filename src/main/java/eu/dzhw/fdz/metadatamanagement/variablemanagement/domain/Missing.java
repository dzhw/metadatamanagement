
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A missing or missing value is a value in a {@link Variable} which represents a reason why no
 * observation ({@link ValidResponse}) has been stored. It also contains its frequency.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Missing {

  /**
   * A (unique in this {@link Variable}) code for this missing.
   * 
   * Must not be empty.
   */
  @NotEmpty(message = "variable-management.error.missing.code.not-null")
  private String code;

  /**
   * A label describing this missing.
   * 
   * Must not contain more than 512 characters.
   */
  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.missing.label.i18n-string-size")
  private I18nString label;

  /**
   * The absolute number of occurrences of this missing.
   * 
   * Must not be empty.
   */
  @NotNull(message = "variable-management.error.missing.absolute-frequency.not-null")
  private Integer absoluteFrequency;

  /**
   * The quotient from absoluteFrequency and {@link Distribution}.totalAbsoluteFrequency.
   * 
   * Must not be empty.
   */
  @NotNull(message = "variable-management.error.missing.relative-frequency.not-null")
  private Double relativeFrequency;
}
