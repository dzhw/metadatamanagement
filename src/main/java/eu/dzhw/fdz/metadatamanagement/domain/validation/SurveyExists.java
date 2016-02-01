package eu.dzhw.fdz.metadatamanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Validate that a survey with the given id exists.
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {SurveyExistsValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SurveyExists {
  
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "surveyexists.message}";
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};
}