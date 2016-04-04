package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Value;

/**
 * A validation checks, that only a valueClass or code is set by a user. One of the fields must be
 * set, but both are not allowed at the same time.
 * 
 * @author dkatzberg
 *
 */
public class ValidCodeOrValueClassValidator
    implements ConstraintValidator<ValidCodeOrValueClass, Value> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidCodeOrValueClass constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Value value, ConstraintValidatorContext context) {

    // get valueClass Status
    boolean emptyValueClass = false;
    if (value.getValueClass() == null || value.getValueClass()
        .isEmpty()) {
      emptyValueClass = true;
    }

    // get empty code status
    boolean emptyCode = false;
    if (value.getCode() == null) {
      emptyCode = true;
    }

    // if both empty -> false
    if (emptyCode && emptyValueClass) {
      return false;
    }

    // if both not empty -> false
    if (!emptyCode && !emptyValueClass) {
      return false;
    }

    // if only one of the both is set -> valid.
    return true;
  }

}
