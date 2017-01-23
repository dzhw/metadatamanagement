package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the statistics of a variable. It checks the third quartile has a numeric string, if the
 * variable has a numeric data type.
 * 
 * @author Daniel Katzberg
 *
 */
public class StatisticsThirdQuartileMustBeAnIsoDateOnDateDataTypeValidator implements
    ConstraintValidator<StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType, Variable> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      StatisticsThirdQuartileMustBeAnIsoDateOnDateDataType constraintAnnotation) {}

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

    if (variable.getDataType().equals(DataTypes.DATE)) {
      //check for an iso standard
      String thirdQuatile = variable.getDistribution().getStatistics().getThirdQuartile();

      try {
        LocalDate.parse(thirdQuatile);
        return true;
      } catch (DateTimeParseException dtpe) {
        return false; // not parsable
      }
      
    }

    // no date, everything is okay.
    return true;
  }

}
