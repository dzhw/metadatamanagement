package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that given value for {@code surveyDataType} matches one of
 * {@link eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDataTypes}.
 */
@Documented
@Constraint(validatedBy = {ValidSurveyDataTypeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSurveyDataType {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "order-management.error.valid-survey-data-type}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
