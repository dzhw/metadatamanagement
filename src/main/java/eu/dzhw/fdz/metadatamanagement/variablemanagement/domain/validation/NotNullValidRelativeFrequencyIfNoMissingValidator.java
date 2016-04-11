package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Value;

/**
 * Validation for the Value. If the value is no missing, the relative frequency is mandatory.
 * 
 * @author Daniel Katzberg
 *
 */
public class NotNullValidRelativeFrequencyIfNoMissingValidator
    implements ConstraintValidator<NotNullValidRelativeFrequencyIfNoMissing, Value> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(NotNullValidRelativeFrequencyIfNoMissing constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Value value, ConstraintValidatorContext context) {

    // is a missing, there is no valid relativ frequency
    if (value.getIsAMissing() && value.getValidRelativeFrequency() == null) {
      return true;
    }

    // if not a missing, the valid relative frequency is mandatory
    if (!value.getIsAMissing() && value.getValidRelativeFrequency() != null) {
      return true;
    }

    return false;
  }

}
