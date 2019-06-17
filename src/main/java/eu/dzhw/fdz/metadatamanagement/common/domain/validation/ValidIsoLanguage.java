package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.common.domain.validation.ValidIsoLanguage.List;

/**
 * Annotation for the validation of a language codes according to ISO 639.
 */
@Documented
@Constraint(validatedBy = {ValidIsoLanguageValidator.class})
@Repeatable(List.class)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIsoLanguage {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation"
  + "common.error.language.not-supported}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
  
  /**
   * Defines several {@link ValidIsoLanguage} annotations on the same element.
   *
   * @see ValidIsoLanguage
   */
  @Target({ElementType.TYPE, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    ValidIsoLanguage[] value();
  }
}
