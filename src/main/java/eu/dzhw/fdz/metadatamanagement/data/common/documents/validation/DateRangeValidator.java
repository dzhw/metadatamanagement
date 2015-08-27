package eu.dzhw.fdz.metadatamanagement.data.common.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.data.common.documents.DateRange;

/**
 * The Validator compares to {@code LocalDate} time stamps and compares for the correct order. The
 * start date have to be before the end date.
 * 
 * @author Daniel Katzberg
 *
 */
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDateRange dateOrder) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {

    // no given date range, so the date range logic is not broken.
    if (dateRange == null) {
      return true;
    }

    // start and end are mandatory for a valid date range but are validated by jsr validator
    if (dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
      return true;
    }

    return dateRange.getStartDate().isBefore(dateRange.getEndDate());
  }
}
