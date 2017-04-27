package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The field has been released before can only be set to true once. If it is set to true, it cannot 
 * changed to false again.
 */
@Documented
@Constraint(validatedBy = {SetHasBeenReleasedBeforeOnlyOnceValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SetHasBeenReleasedBeforeOnlyOnce {
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "setHasBeenReleasedBeforeOnlyOnce}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
