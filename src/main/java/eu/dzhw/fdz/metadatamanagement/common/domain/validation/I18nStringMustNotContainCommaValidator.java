package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Annotation for i18n String (de/en Strings). It ensures that no string contains a ",".
 * 
 * @author René Reitmann
 */
public class I18nStringMustNotContainCommaValidator implements 
    ConstraintValidator<I18nStringMustNotContainComma, I18nString> {
  
  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(I18nStringMustNotContainComma constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString value, ConstraintValidatorContext context) {
    //Empty check
    if (value == null) {
      return true;
    }
    
    if (!(value.getDe() == null) && value.getDe().contains(",")) {
      return false;
    }
    
    if (!(value.getDe() == null) && value.getEn().contains(",")) {
      return false;
    }
    
    return true;
  }

}
