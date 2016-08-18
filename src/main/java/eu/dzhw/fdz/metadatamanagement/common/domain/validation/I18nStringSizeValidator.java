package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Validate the size of the i18n strings.
 * @author Daniel Katzberg
 *
 */
public class I18nStringSizeValidator implements ConstraintValidator<I18nStringSize, I18nString> {

  private int max;
  private int min;
  
  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(I18nStringSize constraintAnnotation) {
    this.max = constraintAnnotation.max();
    this.min = constraintAnnotation.min();
  }

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString value, ConstraintValidatorContext context) {
    
    boolean deValid = false;
    boolean enValid = false;
    
    //Empty check
    if (value == null) {
      return true;
    }
    
    //Check De String
    if (value.getDe() == null) { 
      if (this.min == 0) {
        deValid = true;      
      } 
    } else {
      if (value.getDe().length() >= this.min && value.getDe().length() <= this.max) {
        deValid = true;
      }
    }
    
    //Check En String
    if (value.getEn() == null) { 
      if (this.min == 0) {
        enValid = true;      
      } 
    } else {
      if (value.getEn().length() >= this.min && value.getEn().length() <= this.max) {
        enValid = true;
      }
    }
    
    return deValid && enValid;
  }

}
