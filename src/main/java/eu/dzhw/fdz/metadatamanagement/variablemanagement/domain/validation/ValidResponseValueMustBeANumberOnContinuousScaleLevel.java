package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the valid responses of a variable. It checks the values has a
 * numeric string, if the variable has a continuous scale level.
 * 
 * @author dkatzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidResponseValueMustBeANumberOnContinuousScaleLevelValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResponseValueMustBeANumberOnContinuousScaleLevel {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validResponseValueMustBeANumberOnContinuousScaleLevel.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
