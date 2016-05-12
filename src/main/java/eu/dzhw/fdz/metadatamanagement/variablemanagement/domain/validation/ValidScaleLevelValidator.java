package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;

/**
 * Validator of the scale level. Only values from the class {@link ScaleLevels} are allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidScaleLevelValidator implements ConstraintValidator<ValidScaleLevel, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidScaleLevel constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString scaleLevel, ConstraintValidatorContext context) {

    if (scaleLevel == null) {
      return true;
    }

    if (Strings.isNullOrEmpty(scaleLevel.getDe()) && Strings.isNullOrEmpty(scaleLevel.getEn())) {
      return true;
    }

    // check for scale levels
    return ScaleLevels.ALL.contains(scaleLevel);
  }
}
