package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.Period;

/**
 * Validate that the begin of a period is <= then the end.
 * @author René Reitmann
 */
public class ValidPeriodValidator implements ConstraintValidator<ValidPeriod, Period> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidPeriod constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Period period, ConstraintValidatorContext context) {
    // if one end of the period is not set than it is valid
    if (period.getStart() == null || period.getEnd() == null) {
      return true;
    }
    
    return period.getStart().isBefore(period.getEnd()) || period.getStart().equals(period.getEnd());
  }
}
