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

    // Check for emptyness
    if (dataType == null) {
      return false;
    }

    // Check for emptyness
    if (dataType.getDe() == null || dataType.getEn() == null) {
      return false;
    }

    // check de and en

    // english and german have correct values, but are they valid?
    // Numeric
    if (dataType.getDe()
        .equals(DataTypes.NUMERIC.getDe())
        && dataType.getEn()
          .equals(DataTypes.NUMERIC.getEn())) {
      return true;
    }

    // String
    if (dataType.getDe()
        .equals(DataTypes.STRING.getDe())
        && dataType.getEn()
          .equals(DataTypes.STRING.getEn())) {
      return true;
    }

    return false;
  }

}
