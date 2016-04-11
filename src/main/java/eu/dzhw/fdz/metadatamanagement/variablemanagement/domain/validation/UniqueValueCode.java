package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Ensure that value.code is unique within the variable.
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {UniqueValueCodeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValueCode {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "uniquevaluecode.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
