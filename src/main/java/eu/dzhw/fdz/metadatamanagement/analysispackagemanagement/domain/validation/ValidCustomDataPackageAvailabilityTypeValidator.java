package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.CustomDataPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Validation of availability typs of {@link CustomDataPackage}s.
 */
public class ValidCustomDataPackageAvailabilityTypeValidator
    implements ConstraintValidator<ValidCustomDataPackageAvailabilityType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidCustomDataPackageAvailabilityType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString availabilityType, ConstraintValidatorContext context) {
    return CustomDataPackage.AVAILABLE_AVAILABILITY_TYPES.contains(availabilityType);
  }

}
