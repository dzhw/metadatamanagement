package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.DataTypesProvider;

/**
 * The validator checks a String input is a part of the given data type enumeration. The enumeration
 * is a list of accepted input values with a i18n support.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidDataTypeValidator implements ConstraintValidator<ValidDataType, String> {

  @Autowired
  private DataTypesProvider dataTypesProvider;
  
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
  public boolean isValid(String dataType, ConstraintValidatorContext context) {

    // is not a must -> no input, no error.
    if (dataType == null) {
      return true;
    }

    // Look for accepted data type
    if (this.dataTypesProvider.getDataTypesMap().get(LocaleContextHolder.getLocale())
        .contains(dataType)) {
      return true;
    }

    // return false, no accepted data type found
    return false;
  }

}
