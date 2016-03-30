package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;

/**
 * Validates the name of a id. The pattern is: DataAcquisitionProjectId-VariableName. This validator
 * validates the complete name.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidVariableIdNameValidator
    implements ConstraintValidator<ValidVariableIdName, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidVariableIdName constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    // check for set project id
    if (variable.getDataAcquisitionProjectId() == null || variable.getName() == null) {
      return false;
    }

    return variable.getId()
      .equals(variable.getDataAcquisitionProjectId() + "-" + variable.getName());
  }

}
