package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for i18n String (de/en Strings). It ensures that no string contains a ",".
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {I18nStringMustNotContainCommaValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nStringMustNotContainComma {
  
  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "i18nstringmustnotcontaincomma.message";
  
  /**
   * This contains groups.
   */
  Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[]payload() default {};
}
