package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Requirements;

/**
 * Validates that a project configuration requires either analysis packages or data packages.
 */
public class EitherDataPackagesOrAnalysisPackagesRequiredValidator
    implements ConstraintValidator<EitherDataPackagesOrAnalysisPackagesRequired, Requirements> {
  @Override
  public void initialize(EitherDataPackagesOrAnalysisPackagesRequired constraint) {}

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Requirements requirements, ConstraintValidatorContext context) {
    return requirements.isAnalysisPackagesRequired() ^ requirements.isDataPackagesRequired();
  }
}
