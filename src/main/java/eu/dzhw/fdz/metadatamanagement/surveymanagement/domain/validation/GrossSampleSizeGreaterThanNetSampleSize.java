package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Validate that {@link Survey}.grossSampleSize is greater than or equal to
 * {@link Survey}.sampleSize.
 */
@Documented
@Constraint(validatedBy = {GrossSampleSizeGreaterThanNetSampleSizeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GrossSampleSizeGreaterThanNetSampleSize {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "gross-greater-than-net.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

}
