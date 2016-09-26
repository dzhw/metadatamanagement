package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validation for a question number. The number has to be unique within a instrument.
 * 
 * @author dkatzberg
 *
 */
public class UniqueQuestionNumberInInstrumentValidator implements 
    ConstraintValidator<UniqueQuestionNumberInInstrument, String> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueQuestionNumberInInstrument constraintAnnotation) { }

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   *    javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return true;
  }

}
