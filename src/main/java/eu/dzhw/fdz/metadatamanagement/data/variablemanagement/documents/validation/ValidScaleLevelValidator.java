package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.ScaleLevelProvider;

/**
 * The validator checks a String input is a part of the given scale level enumeration. The
 * enumeration is a list of accepted input values with a i18n support.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidScaleLevelValidator implements ConstraintValidator<ValidScaleLevel, String> {

  /**
   * The ScaleLevelProvider inludes all valid scale level for numeric data types for all supported
   * languages.
   */
  private ScaleLevelProvider scaleLevelProvider;

  /**
   * @param scaleLevelProvider The ScaleLevelProvider inludes all valid scale level for numeric data
   *        types for all supported languages.
   */
  @Autowired
  public ValidScaleLevelValidator(ScaleLevelProvider scaleLevelProvider) {
    this.scaleLevelProvider = scaleLevelProvider;
  }

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
  public boolean isValid(String scaleLevel, ConstraintValidatorContext context) {

    // is not a must -> no input, no error.
    if (scaleLevel == null) {
      return true;
    }

    // Look for accepted scale level
    if (this.scaleLevelProvider.getScaleLevelMap().get(LocaleContextHolder.getLocale())
        .contains(scaleLevel)) {
      return true;
    }

    // return false, no accepted data type found
    return false;
  }
}
