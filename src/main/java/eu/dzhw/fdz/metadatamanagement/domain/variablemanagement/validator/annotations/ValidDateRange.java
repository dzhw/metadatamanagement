package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.DateRangeValidator;

/**
 * This annotation compares two dates and check the correct time line order of the two dates.
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {

  /**
   * Defines the default error message.
   */
//TODO i18n is still missing
  public abstract String message() default "The dates are not in correct order.";
  
}
