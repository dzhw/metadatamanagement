package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.AtomicQuestionTypes;
import eu.dzhw.fdz.metadatamanagement.domain.I18nString;

/**
 * Validation of the consistence of given types. 
 * Is english 'open' and german 'offen' set at same time? If it is not, 
 * the validation going be wrong.
 * Only values from the class {@link AtomicQuestionTypes} allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidAtomicQuestionTypeValidator  
    implements ConstraintValidator<ValidAtomicQuestionType, I18nString> { 


  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAtomicQuestionType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString type, ConstraintValidatorContext context) {
            
    //english and german have correct values, but are they consistent?
    //open
    if (type.getDe().equals(AtomicQuestionTypes.OPEN.getDe()) 
        && type.getEn().equals(AtomicQuestionTypes.OPEN.getEn())) {
      return true;
    }
    
    //close
    if (type.getDe().equals(AtomicQuestionTypes.SINGLE_CHOICE.getDe()) 
        && type.getEn().equals(AtomicQuestionTypes.SINGLE_CHOICE.getEn())) {
      return true;
    }
        
    return false;
  }
}
