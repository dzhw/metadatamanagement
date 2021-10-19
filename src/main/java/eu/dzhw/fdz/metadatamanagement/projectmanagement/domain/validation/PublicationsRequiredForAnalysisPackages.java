package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validates that a project configuration requires publication when analysis packages are required.
 */
@Documented
@Constraint(validatedBy = PublicationsRequiredForAnalysisPackagesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicationsRequiredForAnalysisPackages {
  /**
   * Defines the default error message.
   */
  String message() default "{data-acquisition-project-management.error.data-acquisition-project"
      + ".configuration.publications-required-for-analysis-packages}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
