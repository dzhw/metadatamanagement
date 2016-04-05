package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.AtomicQuestionTypes;

/**
 * Validation of the consistence of given types. 
 * Is english 'open' and german 'offen' set at same time? If it is not, 
 * the validation going be wrong.
 * Only values from the class {@link AtomicQuestionTypes} allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidAtomicQuestionTypeValidator  
    implements ConstraintValidator<ValidAtomicQuestionType, I18nString> { 


  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAtomicQuestionType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString type, ConstraintValidatorContext context) {
    // check for atomic question types
    return AtomicQuestionTypes.ALL.contains(type);
  }
}
