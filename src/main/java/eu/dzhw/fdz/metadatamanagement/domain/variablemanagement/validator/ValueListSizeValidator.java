package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.Value;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.ValidValueListSize;

/**
 * This validator compares to list sizes.
 * 
 * @author Daniel Katzberg
 */
public class ValueListSizeValidator implements ConstraintValidator<ValidValueListSize, Value> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidValueListSize constraintAnnotation) {
    // do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Value value, ConstraintValidatorContext context) {

    if (value == null) {
      return true; // TODO default case. redesign later
    }

    if (value.getValues() == null || value.getValueLabels() == null) {
      return true; // TODO default case. redesign later
    }

    return value.getValues().size() >= value.getValueLabels().size();
  }

}
