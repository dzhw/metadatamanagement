package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Validate the single strings of the i18n strings. If both are not null or empty, return true. 
 * If one or none of the String is set, then return false.
 *  
 * @author Daniel Katzberg
 *
 */
public class I18nStringEntireNotEmptyValidator implements 
    ConstraintValidator<I18nStringEntireNotEmpty, I18nString> {
  
  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(I18nStringEntireNotEmpty constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString value, ConstraintValidatorContext context) {
        
    //Empty check
    if (value == null) {
      return false;
    }
    
    //Check De /En Empty String    
    return !Strings.isNullOrEmpty(value.getDe()) && !Strings.isNullOrEmpty(value.getEn());
  }

}
