package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a string contains an existing ISO 3166-1 alpha-2 country code.
 */
@Constraint(validatedBy = {ValidCountryCodeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCountryCode {
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validcountrycode.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
