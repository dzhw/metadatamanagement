package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageManagementService;
import eu.dzhw.fdz.metadatamanagement.searchmanagement.documents.DataPackageSearchDocument;

/**
 * Ensure that the analyzed {@link DataPackage} is a valid shadow with valid access way.
 * 
 * @author René Reitmann
 */
public class ValidDataPackageShadowValidator
    implements ConstraintValidator<ValidDataPackageShadow, DataPackage> {

  @Autowired
  private DataPackageManagementService dataPackageManagementService;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageShadow constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DataPackage dataPackage, ConstraintValidatorContext context) {
    if (dataPackage == null) {
      return false;
    }
    DataPackageSearchDocument referencedDataPackage =
        (DataPackageSearchDocument) dataPackageManagementService.readSearchDocument(
            dataPackage.getDataPackageMasterId() + "-" + dataPackage.getVersion()).orElse(null);

    if (referencedDataPackage != null
        && referencedDataPackage.getAccessWays().contains(dataPackage.getAccessWay())) {
      return true;
    }
    return false;
  }
}
