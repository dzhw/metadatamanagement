package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;

/**
 * Validates that there is an access way if there is a data package in the product.
 */
class AccessWayNotEmptyForDataPackageValidator
    implements ConstraintValidator<AccessWayNotEmptyForDataPackage, Product> {

  @Override
  public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
    if (product.getDataPackage() == null) {
      return true;
    }
    return StringUtils.hasText(product.getAccessWay());
  }
}
