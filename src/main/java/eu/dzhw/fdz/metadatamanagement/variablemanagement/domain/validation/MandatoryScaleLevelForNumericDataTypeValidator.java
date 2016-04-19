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
    if (variable.getDataType() == null || !variable.getDataType().equals(DataTypes.NUMERIC)) {
      return true;
    }
    
    // string is set, but a scale level too -> false!
    if (variable.getDataType()
        .equals(DataTypes.STRING) && variable.getScaleLevel() != null) {
      return false;
    }

    return variable.getScaleLevel() != null;
  }
}
