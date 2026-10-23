package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.Script;
import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.SoftwarePackages;

/**
 * Validation of software packages of {@link Script}s.
 */
public class ValidSoftwarePackageValidator
    implements ConstraintValidator<ValidSoftwarePackage, String> {
  
  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidSoftwarePackage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String softwarePackage, ConstraintValidatorContext context) {
    return SoftwarePackages.ALL.contains(softwarePackage);
  }

}
