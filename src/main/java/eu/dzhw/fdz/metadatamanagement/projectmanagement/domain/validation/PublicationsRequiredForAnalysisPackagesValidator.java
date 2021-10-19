package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;

/**
 * Validates that a project configuration requires publication when analysis packages are required.
 */
public class PublicationsRequiredForAnalysisPackagesValidator
    implements ConstraintValidator<PublicationsRequiredForAnalysisPackages, Requirements> {
  @Override
  public void initialize(PublicationsRequiredForAnalysisPackages constraint) {}

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Requirements requirements, ConstraintValidatorContext context) {
    if (!requirements.isAnalysisPackagesRequired()) {
      return true;
    }
    return requirements.isPublicationsRequired();
  }
}
