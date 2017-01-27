package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.DataTypes;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validator which ensures that a ScaleLevel is defined for numeric Variables.
 * 
 * @author René Reitmann
 */
public class MandatoryScaleLevelForNumericDataTypeValidator
    implements ConstraintValidator<MandatoryScaleLevelForNumericDataType, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(MandatoryScaleLevelForNumericDataType constraintAnnotation) {}

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
      return false;
    }

    // string is set, but a scale level too -> invalid!
    if (variable.getDataType()
        .equals(DataTypes.STRING) && variable.getScaleLevel() == null) {
      return true;
    }
    
    //date is set, but a scale level too -> invalid!
    if (variable.getDataType()
        .equals(DataTypes.DATE) && variable.getScaleLevel() == null) {
      return true;
    }

    // numeric is set, but no scale level -> invalid!
    if (variable.getDataType()
        .equals(DataTypes.NUMERIC) && variable.getScaleLevel() != null) {
      return true;
    }

    return false;
  }
}
