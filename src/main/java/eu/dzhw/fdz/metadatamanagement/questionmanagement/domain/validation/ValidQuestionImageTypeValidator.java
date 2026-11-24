package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.ImageType;

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
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidQuestionImageType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object, 
   * jakarta.validation.ConstraintValidatorContext)
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
