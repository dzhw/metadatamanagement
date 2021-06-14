package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that an (@link {@link eu.dzhw.fdz.metadatamanagement.common.domain.I18nString}
 * contains values specified by VFDB.
 * <a href="https://mdr.iqb.hu-berlin.de/#/catalog/94d1ae4f-a441-c728-4a03-adb0eb4604af">
 * GNERD: Survey Unit Educational Research (Version 1.0)
 * </a>
 */
@Documented
@Constraint(validatedBy = {ValidUnitValueValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUnitValue {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation"
      + "survey-management.error.population.valid-unit-value}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
