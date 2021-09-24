package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ExternalDataPackage;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Validation of availability typs of {@link ExternalDataPackage}s.
 */
public class ValidExternalDataPackageAvailabilityTypeValidator
    implements ConstraintValidator<ValidExternalDataPackageAvailabilityType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidExternalDataPackageAvailabilityType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString availabilityType, ConstraintValidatorContext context) {
    return ExternalDataPackage.AVAILABLE_AVAILABILITY_TYPES.contains(availabilityType);
  }

}
