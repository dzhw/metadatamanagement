package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates a project update follows business and authorization constraints.
 */
@Documented
@Constraint(validatedBy = ValidDataAcquisitionProjectIdValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDataAcquisitionProjectId {
  /**
   * Defines the default error message.
   */
  String message() default "{data-acquisition-project-management.error.data-acquisition-project"
      + ".id.pattern}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
