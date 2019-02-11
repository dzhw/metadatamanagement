package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.AbstractShadowableRdcDomainObject;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that {@link AbstractShadowableRdcDomainObject} {@code id} starts with
 * {@link AbstractShadowableRdcDomainObject} {@code masterId} and optionally contains
 * a version suffix if the domain object is a shadow copy, otherwise the result
 * will always be {@code true}.
 */
@Documented
@Constraint(validatedBy = ValidDerivedIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDerivedId {

  /**
   * Defines the default error message.
   */
  String message() default "{common.domain.validation.valid-derived-id.error.no-match}";

  /**
   * Groups parameter.
   */
  Class<?>[] groups() default {};

  /**
   * Payload parameter.
   */
  Class<? extends Payload>[] payload() default {};
}

