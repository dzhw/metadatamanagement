package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.DataAvailabilities;

/**
 * Checks the dataAvaibility field for valid values.
 * 
 * @author Daniel Katzberg
 */
public class ValidDataAvailabilityValidator implements 
    ConstraintValidator<ValidDataAvailability, I18nString> {

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataAvailability constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, 
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString dataAvailability, ConstraintValidatorContext context) {
    
    if (dataAvailability == null) {
      return true;
    }

    // check for data avaibility
    return DataAvailabilities.ALL.contains(dataAvailability);
  }

  
}
