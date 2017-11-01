
package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The value includes the value itself, a label and frequencies. There are no calculations of the
 * frequencies.
 * 
 * @author Daniel Katzberg
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class ValidResponse {

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.valid-response.label.i18n-string-size")
  private I18nString label;

  @NotNull(
      message = "variable-management.error.valid-response.absolute-frequency.not-null")
  private Integer absoluteFrequency;

  @NotNull(
      message = "variable-management.error.valid-response.relative-frequency.not-null")
  private Double relativeFrequency;

  @NotEmpty(message = "variable-management.error.valid-response.value.not-null")
  @Size(max = 256, 
      message = "variable-management.error.valid-response.value.size")
  private String value;

  @NotNull(
      message = "variable-management.error.valid-response.validRelativeFrequency.not-null")
  private Double validRelativeFrequency;
}
