package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that either an analysisPackage or a dataPackage is in the product.
 */
@Documented
@Constraint(validatedBy = {EitherAnalysisPackageOrDataPackageOrderedValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EitherAnalysisPackageOrDataPackageOrdered {

  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "order-management.error.either-analysis-package-or-data-package";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
