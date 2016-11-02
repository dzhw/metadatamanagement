package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.DataAvaibilities;

/**
 * Checks the dataAvaibility field for valid values.
 * 
 * @author Daniel Katzberg
 */
public class ValidDataAvaibilityValidator implements 
    ConstraintValidator<ValidDataAvaibility, I18nString> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataAvaibility constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString dataAvaibility, ConstraintValidatorContext context) {
    
    if (dataAvaibility == null) {
      return true;
    }

    // check for data avaibility
    return DataAvaibilities.ALL.contains(dataAvaibility);
  }

  
}
