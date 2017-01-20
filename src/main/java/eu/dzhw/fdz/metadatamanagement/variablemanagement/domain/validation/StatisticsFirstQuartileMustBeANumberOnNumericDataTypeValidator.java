package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the statistics of a variable. It checks the first quartile has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class StatisticsFirstQuartileMustBeANumberOnNumericDataTypeValidator implements
    ConstraintValidator<StatisticsFirstQuartileMustBeANumberOnNumericDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      StatisticsFirstQuartileMustBeANumberOnNumericDataType constraintAnnotation) {}

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
      String firstQuartile = variable.getDistribution().getStatistics().getFirstQuartile();
      // if one value is not number ... send a false.
      if (!firstQuartile.matches(regex)) {
        return false;
      }
    }

    // no numeric, everything is okay.
    return true;
  }

}
