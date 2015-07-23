package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.Enum;

/**
 * This is the validation of the annoation Enum. It checks for the case that a given String is part
 * of a enumeration.
 * 
 * @see Enum
 * 
 * @author Daniel Katzberg
 *
 */
public class EnumValidator implements ConstraintValidator<Enum, String> {

  /**
   * A reference Object of the annotation Enum.
   */
  private Enum annotationEnum;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(Enum annotationEnum) {
    this.annotationEnum = annotationEnum;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String valueForValidation,
      ConstraintValidatorContext constraintValidatorContext) {

    Object[] enumValues = this.annotationEnum.enumClass().getEnumConstants();

    // check for null value
    if (enumValues == null) {
      return false;
    }

    // check the given String at enumValues
    for (Object enumValue : enumValues) {
      if (valueForValidation.equals(enumValue.toString())) {
        return true;
      }
    }

    return false;
  }
}
