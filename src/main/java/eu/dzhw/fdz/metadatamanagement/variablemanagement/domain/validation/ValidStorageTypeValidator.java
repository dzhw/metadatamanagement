package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.StorageTypes;

/**
 * Validator for the storage type of a variable. Only valued from the {@link StorageTypes} class are
 * allowed.
 */
public class ValidStorageTypeValidator implements ConstraintValidator<ValidStorageType, String> {

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStorageType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String storageType, ConstraintValidatorContext context) {

    // check for storage types
    return StorageTypes.ALL.contains(storageType);
  }

}
