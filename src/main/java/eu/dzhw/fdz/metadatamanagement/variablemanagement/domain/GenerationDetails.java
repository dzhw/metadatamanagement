package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.io.Serializable;

import javax.validation.constraints.Size;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.NotEmptyGenerationDetailsDescriptionOrRule;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.RuleExpressionLanguageAndRuleFilledOrEmpty;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidRuleExpressionLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generation details describe how a {@link Variable} was generated from one or more input
 * {@link Variable}s.
 */
@NotEmptyGenerationDetailsDescriptionOrRule(
    message = "variable-management.error.generation-details."
        + "not-empty-generation-details-description-or-rule")
@RuleExpressionLanguageAndRuleFilledOrEmpty(
    message = "variable-management.error.generation-details."
        + "rule-expression-language-and-rule-filled-or-empty")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class GenerationDetails implements Serializable {

  private static final long serialVersionUID = -3981980315577871905L;

  /**
   * A description of this generation rule.
   * 
   * Must not contain more than 2048 characters
   */
  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.generation-details.description.i18n-string-size")
  private I18nString description;

  /**
   * The computation rule in the ruleExpressionLanguage which was used to generate this
   * {@link Variable}.
   * 
   * Must not contain more than 1 MB characters.
   */
  @Size(max = StringLengths.X_LARGE,
      message = "variable-management.error.generation-details.rule.size")
  private String rule;

  /**
   * The language which was used to describe this rule.
   * 
   * Must be one of {@link RuleExpressionLanguages}.
   */
  @ValidRuleExpressionLanguage(
      message = "variable-management.error.generation-details."
          + "rule-expression-language.valid-rule-expression-language")
  private String ruleExpressionLanguage;
}
