package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

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
 * Generation Details.
 *
 * @author Daniel Katzberg
 *
 */
@NotEmptyGenerationDetailsDescriptionOrRule(
    message = "variable-management.error.generation-details."
        + "not-empty-generation-details-description-or-rule")
@RuleExpressionLanguageAndRuleFilledOrEmpty(
    message = "variable-management.error.generation-details."
        + "rule-expression-language-and-rule-filled-or-empty")
@Data
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class GenerationDetails {

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.generation-details.description.i18n-string-size")
  private I18nString description;

  @Size(max = StringLengths.X_LARGE,
      message = "variable-management.error.generation-details.rule.size")
  private String rule;

  @ValidRuleExpressionLanguage(
      message = "variable-management.error.generation-details."
          + "rule-expression-language.valid-rule-expression-language")
  private String ruleExpressionLanguage;
}
