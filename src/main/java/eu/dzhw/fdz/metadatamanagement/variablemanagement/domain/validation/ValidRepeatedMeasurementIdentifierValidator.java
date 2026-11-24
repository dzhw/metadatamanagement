package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidRepeatedMeasurementIdentifier constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
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
