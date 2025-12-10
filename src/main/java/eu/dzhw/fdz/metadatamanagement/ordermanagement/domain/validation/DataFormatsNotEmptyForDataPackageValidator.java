package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;

/**
 * Validates that there is at least one data format if there is a data package in the product.
 */
class DataFormatsNotEmptyForDataPackageValidator
    implements ConstraintValidator<DataFormatsNotEmptyForDataPackage, Product> {

  @Override
  public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
    if (product.getDataPackage() == null) {
      return true;
    }
    return product.getDataFormats() != null && !product.getDataFormats().isEmpty();
  }
}
