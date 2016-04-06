package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validate that the begin of a period is <= then the end.
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
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validperiod.message}";
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};

}
