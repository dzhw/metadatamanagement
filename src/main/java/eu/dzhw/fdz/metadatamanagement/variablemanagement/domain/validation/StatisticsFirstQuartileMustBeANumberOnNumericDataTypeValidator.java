package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.math.NumberUtils;

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

    if (variable == null || variable.getDataType() == null || variable.getDistribution() == null
        || variable.getDistribution().getStatistics() == null
        || variable.getDistribution().getStatistics().getFirstQuartile() == null
        || !variable.getDataType().equals(DataTypes.NUMERIC)) {
      return true;
    }

    String firstQuartile = variable.getDistribution().getStatistics().getFirstQuartile();
    try {
      NumberUtils.createNumber(firstQuartile);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
