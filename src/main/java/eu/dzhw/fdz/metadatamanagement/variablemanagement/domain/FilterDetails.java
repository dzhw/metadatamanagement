package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidFilterExpressionLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FilterDetails of variable.
 * 
 * @author Daniel Katzberg
 *
 */
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class FilterDetails {
  
  @NotEmpty(message = "variable-management.error.filter-details.expression.not-empty")
  @Size(max = StringLengths.LARGE, 
      message = "variable-management.error.filter-details.expression.size")
  private String expression;

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.filter-details.description.i18n-string-size")
  private I18nString description;

  @NotEmpty(message = "variable-management.error.filter-details." 
      + "expression-language.not-empty")
  @ValidFilterExpressionLanguage(
      message = "variable-management.error.filter-details." 
          + "expression-language.valid-filter-expression-language")
  private String expressionLanguage;
}
