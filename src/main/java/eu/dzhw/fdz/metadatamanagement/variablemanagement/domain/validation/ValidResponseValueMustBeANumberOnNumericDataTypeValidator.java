package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the valid responses of a variable. It checks the values has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidResponseValueMustBeANumberOnNumericDataTypeValidator implements
    ConstraintValidator<ValidResponseValueMustBeANumberOnNumericDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      ValidResponseValueMustBeANumberOnNumericDataType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    if (variable == null || variable.getDataType() == null
        || variable.getDistribution() == null
        || variable.getDistribution().getValidResponses() == null) {
      return true;
    }

    if (variable.getDataType()
        .equals(DataTypes.NUMERIC)) {
      String regex = "-?\\d+(\\.\\d+)?";
      for (ValidResponse validResponse : variable.getDistribution().getValidResponses()) {
        // if one value is not number ... send a false.
        if (!validResponse.getValue().matches(regex)) {
          return false;
        }
      }
    }

    // no numeric, everything is okay.
    return true;
  }

}
