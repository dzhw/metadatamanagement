package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.CustomDataPackage;

/**
 * Annotation for the validation of access ways of {@link CustomDataPackage}s.
 */
@Documented
@Constraint(validatedBy = {ValidCustomDataPackageAccesWayValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomDataPackageAccessWay {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "analysis-package.error.custom-data-package.invalid-access-way}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
