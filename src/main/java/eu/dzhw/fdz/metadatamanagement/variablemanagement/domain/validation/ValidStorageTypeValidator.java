package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.StorageTypes;

/**
 * Validator for the storage type of a variable. Only valued from the {@link StorageTypes} class are
 * allowed.
 */
public class ValidStorageTypeValidator implements ConstraintValidator<ValidStorageType, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStorageType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String storageType, ConstraintValidatorContext context) {

    // check for storage types
    return StorageTypes.ALL.contains(storageType);
  }

}
