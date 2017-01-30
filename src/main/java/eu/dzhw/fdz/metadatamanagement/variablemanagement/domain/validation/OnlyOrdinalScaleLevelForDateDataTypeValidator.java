package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validator which ensures that Ensure that date variables have only a ordinal scale level.
 * 
 * @author René Reitmann
 */
public class OnlyOrdinalScaleLevelForDateDataTypeValidator
    implements ConstraintValidator<OnlyOrdinalScaleLevelForDateDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(OnlyOrdinalScaleLevelForDateDataType constraintAnnotation) {}

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
    
    //if no datatype date, this validator is not important. 
    if (!variable.getDataType().equals(DataTypes.DATE)) {
      return true;
    }
        
    //date is set (if not, this code isn't reachable), but a no scale level -> invalid!
    return ScaleLevels.ORDINAL.equals(variable.getScaleLevel());
  }
}
