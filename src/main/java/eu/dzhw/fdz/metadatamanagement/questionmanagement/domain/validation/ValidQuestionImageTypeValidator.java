package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.ImageType;

/**
 * Validator of the question image type. Only the png value from the enum {@link ImageType} is 
 * allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidQuestionImageTypeValidator implements 
    ConstraintValidator<ValidQuestionImageType, ImageType> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidQuestionImageType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(ImageType imageType, ConstraintValidatorContext context) {
        
    if (imageType == null) {
      return true;
    }

    // check for scale levels
    return imageType.equals(ImageType.PNG);
  }

}
