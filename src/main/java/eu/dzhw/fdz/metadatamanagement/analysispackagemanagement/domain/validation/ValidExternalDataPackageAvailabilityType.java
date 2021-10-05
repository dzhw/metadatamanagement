package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.ExternalDataPackage;

/**
 * Annotation for the validation of availability types of {@link ExternalDataPackage}s.
 */
@Documented
@Constraint(validatedBy = {ValidExternalDataPackageAvailabilityTypeValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExternalDataPackageAvailabilityType {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "analysis-package.error.external-data-package.invalid-availability-type}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
