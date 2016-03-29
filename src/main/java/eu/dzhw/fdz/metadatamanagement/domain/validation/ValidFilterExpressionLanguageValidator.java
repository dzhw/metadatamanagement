package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.FilterExpressionLanguages;

/**
 * Validator for the filter expression languages of a variable. Only valued from the
 * {@link FilterExpressionLanguages} class are allowed.
 * 
 * @author dkatzberg
 *
 */
public class ValidFilterExpressionLanguageValidator
    implements ConstraintValidator<ValidFilterExpressionLanguage, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidFilterExpressionLanguage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String filterExpressionLanguage, ConstraintValidatorContext context) {

    // mandatory field
    if (filterExpressionLanguage == null || filterExpressionLanguage.isEmpty()) {
      return false;
    }

    // check all expression languages
    switch (filterExpressionLanguage) {
      // All allowed values are okay. break switch.
      case FilterExpressionLanguages.SPEL:
        break;
      case FilterExpressionLanguages.STATA:
        break;
      // Not valid expression language
      default:
        return false;
    }

    // expression language is okay
    return true;
  }

}
