package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.RuleExpressionLanguages;

/**
 * Validator for the rule expression languages of a variable. Only valued from the
 * {@link RuleExpressionLanguages} class are allowed.
 * 
 * @author Daniel Katzberg
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

    // It is allowed, that the rule expression language is empty.
    if (Strings.isNullOrEmpty(ruleExpressionLanguage)) {
      return true;
    }

    // check for rule expression languages
    return RuleExpressionLanguages.ALL.contains(ruleExpressionLanguage);
  }

}
