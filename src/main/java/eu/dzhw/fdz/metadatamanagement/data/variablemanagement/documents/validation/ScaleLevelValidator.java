package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types.ScaleLevel;

/**
 * The validator checks a String input is a part of the given scale level enumeration. The
 * enumeration is a list of accepted input values with a i18n support.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevelValidator implements ConstraintValidator<ValidScaleLevel, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidScaleLevel constraintAnnotation) {
    // Do nothing.
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {

    // is not a must -> no input, no error.
    if (value == null) {
      return true;
    }

    // Look for accepted scale level
    if (ScaleLevel.getScaleLevel().getScaleLevelMap().get(LocaleContextHolder.getLocale())
        .contains(value)) {
      return true;
    }

    // return false, no accepted data type found
    return false;
  }
}
