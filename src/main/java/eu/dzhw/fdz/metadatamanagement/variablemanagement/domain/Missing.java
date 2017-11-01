
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The missing includes a code, a label and frequencies. There are no calculations of the
 * frequencies. This represent a missing and not a valid response. Use {@code ValidResponse} for the
 * representation of valid responses.
 *
 * @author Daniel Katzberg
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class Missing {

  @NotEmpty(message = "variable-management.error.missing.code.not-null")
  private String code;

  @I18nStringSize(max = StringLengths.MEDIUM,
      message = "variable-management.error.missing.label.i18n-string-size")
  private I18nString label;

  @NotNull(message = "variable-management.error.missing.absolute-frequency.not-null")
  private Integer absoluteFrequency;

  @NotNull(message = "variable-management.error.missing.relative-frequency.not-null")
  private Double relativeFrequency;
}
