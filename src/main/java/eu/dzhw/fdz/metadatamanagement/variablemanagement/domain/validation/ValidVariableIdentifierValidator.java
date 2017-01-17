package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This validator checks a panelIdentifier.
 *
 */
public class ValidVariableIdentifierValidator
    implements ConstraintValidator<ValidVariableIdentifier, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidVariableIdentifier constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    if (variable.getPanelIdentifier() == null) {
      return true;
    }
    return variable.getPanelIdentifier().contains(variable
        .getDataAcquisitionProjectId() + "-ds" + variable.getDataSetNumber() + "-");
  }
}
