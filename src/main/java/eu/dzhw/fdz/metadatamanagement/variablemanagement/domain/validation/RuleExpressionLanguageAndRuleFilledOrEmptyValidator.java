package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.GenerationDetails;

/**
 * Validation of the rule expression language field. If a the rule field is set in the generation
 * details, the rule expression language have to be non empty.
 * 
 * @author dkatzberg
 *
 */
public class RuleExpressionLanguageAndRuleFilledOrEmptyValidator
    implements ConstraintValidator<RuleExpressionLanguageAndRuleFilledOrEmpty, GenerationDetails> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(RuleExpressionLanguageAndRuleFilledOrEmpty constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(GenerationDetails generationDetails, ConstraintValidatorContext context) {

    // Rule both field are empty...
    if (Strings.isNullOrEmpty(generationDetails.getRule())
        && Strings.isNullOrEmpty(generationDetails.getRuleExpressionLanguage())) {
      return true;
    }

    // ... or both are filled.
    if (!Strings.isNullOrEmpty(generationDetails.getRule())
        && !Strings.isNullOrEmpty(generationDetails.getRuleExpressionLanguage())) {
      return true;
    }

    // all other cases.
    return false;
  }

}
