package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.domain.ScaleLevels;

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

 // Check for emptyness
    if (scaleLevel == null) {
      return true;
    }

    //english and german have correct values, but are they consistent?
    // nominal
    if (scaleLevel.getDe()
        .equals(ScaleLevels.NOMINAL.getDe())
        && scaleLevel.getEn()
          .equals(ScaleLevels.NOMINAL.getEn())) {
      return true;
    }
    
    // ordinal
    if (scaleLevel.getDe()
        .equals(ScaleLevels.ORDINAL.getDe())
        && scaleLevel.getEn()
          .equals(ScaleLevels.ORDINAL.getEn())) {
      return true;
    }

    // continous
    if (scaleLevel.getDe()
        .equals(ScaleLevels.CONTINOUS.getDe())
        && scaleLevel.getEn()
          .equals(ScaleLevels.CONTINOUS.getEn())) {
      return true;
    }
        
    return false;
  }
}
