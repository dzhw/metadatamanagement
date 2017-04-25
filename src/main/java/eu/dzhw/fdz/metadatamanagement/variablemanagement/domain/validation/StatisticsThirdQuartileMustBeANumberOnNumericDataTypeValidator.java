package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.math.NumberUtils;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the statistics of a variable. It checks the third quartile has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class StatisticsThirdQuartileMustBeANumberOnNumericDataTypeValidator implements
    ConstraintValidator<StatisticsThirdQuartileMustBeANumberOnNumericDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      StatisticsThirdQuartileMustBeANumberOnNumericDataType constraintAnnotation) {}

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
        || variable.getDistribution().getStatistics() == null
        || variable.getDistribution().getStatistics().getThirdQuartile() == null
        || !variable.getDataType().equals(DataTypes.NUMERIC)) {
      return true;
    }

    String thirdQuartile = variable.getDistribution().getStatistics().getThirdQuartile();
    try {
      NumberUtils.createNumber(thirdQuartile);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
