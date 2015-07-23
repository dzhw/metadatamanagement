package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.DateRange;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations.DateOrder;

/**
 * The Validator compares to {@code LocalDate} time stamps and compares for the correct order. The
 * start date have to be before the end date.
 * 
 * @author Daniel Katzberg
 *
 */
public class DateOrderValidator implements ConstraintValidator<DateOrder, DateRange> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(DateOrder dateOrder) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DateRange value, ConstraintValidatorContext context) { 
    return value.getStartDate().isBefore(value.getEndDate());
  }
}
