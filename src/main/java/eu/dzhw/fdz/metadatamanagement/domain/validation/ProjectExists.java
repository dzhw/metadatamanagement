package eu.dzhw.fdz.metadatamanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation is a validation annotation for a project name. The validation checks, if a
 * project name is valid and exists in the mongo database.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ProjectExistsValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectExists {
  
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validateprojectname.message}";
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};

}
