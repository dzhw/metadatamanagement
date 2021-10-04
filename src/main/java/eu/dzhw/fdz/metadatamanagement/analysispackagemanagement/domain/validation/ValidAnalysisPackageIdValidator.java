package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.AnalysisPackage;

/**
 * Ensure that an analysis package id is equal to "ana-{{project id}}$".
 * 
 * @author René Reitmann
 */
public class ValidAnalysisPackageIdValidator
    implements ConstraintValidator<ValidAnalysisPackageId, AnalysisPackage> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAnalysisPackageId constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(AnalysisPackage analyisPackage, ConstraintValidatorContext context) {
    if (analyisPackage.isShadow()) {
      return true;
    } else {
      return analyisPackage.getId()
          .equals("ana-" + analyisPackage.getDataAcquisitionProjectId() + "$");
    }
  }
}
