package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that there is an access way if there is a data package in the product.
 */
@Documented
@Constraint(validatedBy = {AccessWayNotEmptyForDataPackageValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessWayNotEmptyForDataPackage {

  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "order-management.error.access-way-empty";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
