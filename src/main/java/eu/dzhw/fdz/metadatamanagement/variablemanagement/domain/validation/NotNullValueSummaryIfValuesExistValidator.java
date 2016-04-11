package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validation for the variable: If values added to the variable, the value summary is a mandatory
 * field.
 * 
 * @author Daniel Katzberg
 *
 */
public class NotNullValueSummaryIfValuesExistValidator
    implements ConstraintValidator<NotNullValueSummaryIfValuesExist, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(NotNullValueSummaryIfValuesExist constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    // No variable -> true
    if (variable == null) {
      return true;
    }

    // No values, no need for value summary
    if (variable.getValues() == null) {
      return true;
    }

    // If values are added but no value summary is not valid
    if (variable.getValues()
        .size() > 0 && variable.getValueSummary() == null) {
      return false;
    }

    // other cases, like the size for values list is 0...
    return true;
  }

}
