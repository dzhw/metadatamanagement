package eu.dzhw.fdz.metadatamanagement.domain;

import com.google.common.base.MoreObjects;

import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 * Generation Details.
 * 
 * @author Daniel Katzberg
 *
 */
@GeneratePojoBuilder(intoPackage = "eu.dzhw.fdz.metadatamanagement.domain.builders")
public class GenerationDetails {

  private I18nString description;

  private String rule;

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
