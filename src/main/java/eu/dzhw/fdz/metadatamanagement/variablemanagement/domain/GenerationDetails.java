package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.common.base.MoreObjects;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.validation.I18nStringSize;
import eu.dzhw.fdz.metadatamanagement.domain.validation.StringLengths;
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
public class GenerationDetails {

  @I18nStringSize(max = StringLengths.LARGE,
      message = "{error.generationDetails.description.i18nStringSize}")
  private I18nString description;

  @NotEmpty(message = "{error.generationDetails.rule.notEmpty}")
  @Size(max = StringLengths.LARGE, message = "{error.generationDetails.rule.size}")
  private String rule;

  @NotEmpty(message = "{error.generationDetails.ruleExpressionLanguage.notEmpty}")
  @ValidRuleExpressionLanguage(
      message = "{error.generationDetails.ruleExpressionLanguage.validRuleExpressionLanguage}")
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
