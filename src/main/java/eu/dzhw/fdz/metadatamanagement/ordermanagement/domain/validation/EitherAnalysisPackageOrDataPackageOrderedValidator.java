package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;

/**
 * Validates that either an analysisPackage or a dataPackage is in the product.
 */
class EitherAnalysisPackageOrDataPackageOrderedValidator
    implements ConstraintValidator<EitherAnalysisPackageOrDataPackageOrdered, Product> {

  @Override
  public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
    return (product.getDataPackage() != null)
        ^ product.getAnalysisPackage() != null;
  }
}
