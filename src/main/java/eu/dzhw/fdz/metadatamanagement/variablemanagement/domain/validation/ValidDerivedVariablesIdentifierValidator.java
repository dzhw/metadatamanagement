package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * This validator checks a derived variables identifier.
 *
 * @author Daniel Katzberg
 */
public class ValidDerivedVariablesIdentifierValidator
    implements ConstraintValidator<ValidDerivedVariablesIdentifier, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDerivedVariablesIdentifier constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    if (variable.getDerivedVariablesIdentifier() == null) {
      return true;
    }
    return variable.getDerivedVariablesIdentifier().contains(variable
        .getDataAcquisitionProjectId() + "-ds" + variable.getDataSetNumber() + "-");
  }
}
