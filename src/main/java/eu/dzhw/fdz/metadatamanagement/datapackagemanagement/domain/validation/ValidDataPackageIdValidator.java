package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;

/**
 * Ensure that a dataPackage id is equal to its project id.
 * 
 * @author René Reitmann
 */
public class ValidDataPackageIdValidator
    implements ConstraintValidator<ValidDataPackageId, DataPackage> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageId constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataPackage dataPackage, ConstraintValidatorContext context) {
    if (dataPackage.isShadow()) {
      return true;
    } else {
      return dataPackage.getId().equals("stu-" + dataPackage.getDataAcquisitionProjectId() + "$");
    }
  }
}
