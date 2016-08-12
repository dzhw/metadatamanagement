package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.common.domain.validation.StringLengths;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.NotEmptyGenerationDetailsDescriptionOrRule;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.RuleExpressionLanguageAndRuleFilledOrEmpty;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation.ValidRuleExpressionLanguage;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Generation Details.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(
    intoPackage = "eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.builders")
@NotEmptyGenerationDetailsDescriptionOrRule(
    message = "variable-management.error.generationDetails." 
        + "notEmptyGenerationDetailsDescriptionOrRule")
@RuleExpressionLanguageAndRuleFilledOrEmpty(
    message = "variable-management.error.generationDetails." 
        + "ruleExpressionLanguageAndRuleFilledOrEmpty")
public class GenerationDetails {

  @I18nStringSize(max = StringLengths.LARGE,
      message = "variable-management.error.generationDetails.description.i18n-string-size")
  private I18nString description;

  @Size(max = StringLengths.X_LARGE, 
      message = "variable-management.error.generationDetails.rule.size")
  private String rule;

  @ValidRuleExpressionLanguage(
      message = "variable-management.error.generationDetails." 
          + "ruleExpressionLanguage.validRuleExpressionLanguage")
  private String ruleExpressionLanguage;

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("description", description)
      .add("rule", rule)
      .add("ruleExpressionLanguage", ruleExpressionLanguage)
      .toString();
  }

  /* GETTER / SETTER */
  public I18nString getDescription() {
    return description;
  }

  public void setDescription(I18nString description) {
    this.description = description;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
  }

  public String getRuleExpressionLanguage() {
    return ruleExpressionLanguage;
  }

  public void setRuleExpressionLanguage(String ruleExpressionLanguage) {
    this.ruleExpressionLanguage = ruleExpressionLanguage;
  }
}
