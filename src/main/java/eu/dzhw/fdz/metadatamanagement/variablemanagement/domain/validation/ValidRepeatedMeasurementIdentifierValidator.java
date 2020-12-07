package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This validator checks the repeated measurement identifier.
 *
 */
public class ValidRepeatedMeasurementIdentifierValidator
    implements ConstraintValidator<ValidRepeatedMeasurementIdentifier, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidRepeatedMeasurementIdentifier constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    if (variable.isShadow()) {
      return true;
    } else {
      if (variable.getRepeatedMeasurementIdentifier() == null) {
        return true;
      }
      return variable.getRepeatedMeasurementIdentifier().contains(variable
          .getDataAcquisitionProjectId() + "-ds" + variable.getDataSetNumber() + "-");
    }
  }
}
