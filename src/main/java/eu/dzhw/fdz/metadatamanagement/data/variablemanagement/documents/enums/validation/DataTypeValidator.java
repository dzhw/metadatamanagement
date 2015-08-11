package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.enums.DataType;

/**
 * The validator checks a String input is a part of the given enumeration. The enumeration is a list
 * of accepted input values with a i18n support.
 * 
 * @author Daniel Katzberg
 *
 */
public class DataTypeValidator implements ConstraintValidator<ValidDataType, String> {
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataType constraintAnnotation) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // is not a must -> no input, no error.
    if (value == null) {
      return true;
    }

    // Look for accepted data type
    for (DataType dataType : DataType.values()) {
      if (dataType.getI18nValue().equalsIgnoreCase(value)) {
        return true;
      }
    }

    // return false, no accepted data type found
    return false;
  }

}
