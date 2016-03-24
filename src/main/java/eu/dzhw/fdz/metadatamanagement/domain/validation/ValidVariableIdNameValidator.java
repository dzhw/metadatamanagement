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
    if (variable.getDataAcquisitionProjectId() == null) {
      return false;
    }

    String[] splittedId = variable.getId()
      .split("-");

    // The length has to be 2. Before the minus has to be the project id and after the minus has to
    // be the variable name.
    if (splittedId.length != 2) {
      return false;
    }

    // check for correct project id and variable name
    if (variable.getDataAcquisitionProjectId()
        .equals(splittedId[0])
        && variable.getName()
          .equals(splittedId[1])) {
      return true;
    }

    // the project id or the name is in the id not valid.
    return false;
  }

}
