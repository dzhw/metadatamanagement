package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;

/**
 * Validator for the data type of a variable. Only valued from the {@link DataTypes} class are
 * allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidDataTypeValidator implements ConstraintValidator<ValidDataType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString dataType, ConstraintValidatorContext context) {

    // check for data types
    return DataTypes.ALL.contains(dataType);
  }

}
