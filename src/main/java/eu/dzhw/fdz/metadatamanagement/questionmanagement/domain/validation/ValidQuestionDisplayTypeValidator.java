package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.DisplayType;

/**
 * Validator of the question display type. Only the desktop and mobile value from the enum 
 * {@link DisplayType} is allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidQuestionDisplayTypeValidator implements 
    ConstraintValidator<ValidQuestionDisplayType, DisplayType> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidQuestionDisplayType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DisplayType displayType, ConstraintValidatorContext context) {
        
    if (displayType == null) {
      return true;
    }

    // check for display type
    return displayType.equals(DisplayType.DIGITAL) || displayType.equals(DisplayType.PAPER);
  }

}
