package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validate that the begin of a period is less than or equal to the end.
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {ValidPeriodValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPeriod {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validperiod.message}";
  /**
   * This contains groups.
   */
  Class<?>[]groups() default {};
  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[]payload() default {};

}
