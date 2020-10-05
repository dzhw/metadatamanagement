package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.FilterExpressionAndLanguageNotEmpty;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidFilterExpressionLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Filter details of a {@link Variable} describe the condition which must have evaluated to true
 * before a participant was asked a {@link Question} resulting in this {@link Variable}. All
 * participants for which the conditions evaluates to false will have a {@link Missing} in this
 * {@link Variable}.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@FilterExpressionAndLanguageNotEmpty
public class FilterDetails implements Serializable {

  private static final long serialVersionUID = 801116537746296203L;

  /**
   * A technical expression describing the condition which must have evaluated to true. The
   * expression is given in the expressionLanguage.
   * 
   * Can be empty and must not contain more than 2048 characters.
   */
  @Size(max = StringLengths.LARGE,
      message = "variable-management.error.filter-details.expression.size")
  private String expression;

  /**
   * A description of this filter condition. Markdown is supported.
   * 
   * Must not contain more than 2048 characters
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.filter-details.description.i18n-string-size")
  private I18nString description;

  /**
   * The name of the language in which the expression was given.
   * 
   * Can be empty if and only if expression is empty. If present must be one of
   * {@link FilterExpressionLanguages}.
   */
  @ValidFilterExpressionLanguage(
      message = "variable-management.error.filter-details."
          + "expression-language.valid-filter-expression-language")
  private String expressionLanguage;
}
