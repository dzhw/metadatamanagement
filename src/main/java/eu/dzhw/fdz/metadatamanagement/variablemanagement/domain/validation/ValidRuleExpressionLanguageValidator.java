package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;

/**
 * Validator for the rule expression languages of a variable. Only valued from the
 * {@link RuleExpressionLanguages} class are allowed.
 * 
 * @author dkatzberg
 *
 */
public class ValidRuleExpressionLanguageValidator
    implements ConstraintValidator<ValidRuleExpressionLanguage, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidRuleExpressionLanguage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String ruleExpressionLanguage, ConstraintValidatorContext context) {

    // check for rule expression languages
    return RuleExpressionLanguages.ALL.contains(ruleExpressionLanguage);
  }

}
