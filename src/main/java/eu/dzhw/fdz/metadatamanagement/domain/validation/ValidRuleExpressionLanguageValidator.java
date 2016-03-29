package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.RuleExpressionLanguages;

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

    // mandatory field
    if (ruleExpressionLanguage == null || ruleExpressionLanguage.isEmpty()) {
      return false;
    }

    // check all expression languages
    switch (ruleExpressionLanguage) {
      // All allowed values are okay. break switch.
      case RuleExpressionLanguages.R:
        break;
      case RuleExpressionLanguages.STATA:
        break;
      // Not valid expression language
      default:
        return false;
    }

    // expression language is okay
    return true;
  }

}
