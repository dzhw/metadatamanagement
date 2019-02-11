package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Verifies that master id matches the given pattern.
 */
@Documented
@Constraint(validatedBy = ValidMasterIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMasterId {

  /**
   * Pattern to apply to the domain object's id.
   * @return Regular expression pattern
   */
  String pattern();

  /**
   * Defines the default error message.
   */
  String message() default "{common.domain.validation.valid-master-id.error.pattern}";

  /**
   * Groups parameter.
   */
  Class<?>[] groups() default {};

  /**
   * Payload parameter.
   */
  Class<? extends Payload>[] payload() default {};
}
