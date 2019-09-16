package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

/**
 * Validates that {@link AbstractShadowableRdcDomainObject} {@code hidden} is true if and only if
 * the domain object is a shadow and has a successor.
 */
@Documented
@Constraint(validatedBy = ValidHiddenShadowValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidHiddenShadow {

  /**
   * Defines the default error message.
   */
  String message() default "{common.domain.validation.error.invalid-hidden-shadow}";

  /**
   * Groups parameter.
   */
  Class<?>[] groups() default {};

  /**
   * Payload parameter.
   */
  Class<? extends Payload>[] payload() default {};
}
