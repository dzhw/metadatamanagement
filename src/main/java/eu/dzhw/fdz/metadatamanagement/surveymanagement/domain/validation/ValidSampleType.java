package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a {@link eu.dzhw.fdz.metadatamanagement.common.domain.I18nString}
 * matches the controlled VFDB vocabulary.
 * <a href="https://mdr.iqb.hu-berlin.de/#/catalog/1d791cc7-6d8d-dd35-b1ef-0eec9c31bbb5">
 * Catalog: GNERD: Sampling Procedure Educational Research (Version 1.0)
 * </a>
 */
@Documented
@Constraint(validatedBy = {ValidSampleTypeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSampleType {
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "valid-sample-type.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
