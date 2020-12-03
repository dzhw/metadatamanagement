package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validator which ensures that date variables have only a nominal, ordinal or interval scale level.
 * 
 * @author René Reitmann
 */
public class RestrictedScaleLevelForDateDataTypeValidator
    implements ConstraintValidator<RestrictedScaleLevelForDateDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(RestrictedScaleLevelForDateDataType constraintAnnotation) {}

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

    // data type is mandatory
    if (variable.getDataType() == null) {
      return true;
    }

    // if no datatype date, this validator is not important.
    if (!variable.getDataType().equals(DataTypes.DATE)) {
      return true;
    }

    return ScaleLevels.ORDINAL.equals(variable.getScaleLevel())
        || ScaleLevels.INTERVAL.equals(variable.getScaleLevel())
        || ScaleLevels.NOMINAL.equals(variable.getScaleLevel());
  }
}
