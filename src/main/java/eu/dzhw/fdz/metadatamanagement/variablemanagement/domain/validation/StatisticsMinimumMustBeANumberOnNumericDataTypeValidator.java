package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the statistics of a variable. It checks the minimum has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class StatisticsMinimumMustBeANumberOnNumericDataTypeValidator implements
    ConstraintValidator<StatisticsMinimumMustBeANumberOnNumericDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      StatisticsMinimumMustBeANumberOnNumericDataType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    if (variable == null) {
      return true;
    }

    if (variable.getDataType() == null) {
      return true;
    }

    if (variable.getDistribution() == null) {
      return true;
    }

    if (variable.getDistribution().getStatistics() == null) {
      return true;
    }

    if (variable.getDataType()
        .equals(DataTypes.NUMERIC)) {
      String regex = "-?\\d+(\\.\\d+)?";
      String minimum = variable.getDistribution().getStatistics().getMinimum();
      // if one value is not number ... send a false.
      if (!minimum.matches(regex)) {
        return false;
      }
    }

    // no numeric, everything is okay.
    return true;
  }

}
