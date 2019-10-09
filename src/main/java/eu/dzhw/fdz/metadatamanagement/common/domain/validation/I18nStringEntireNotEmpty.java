package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for i18n String (de/en Strings). It checks that both string are set.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {I18nStringEntireNotEmptyValidator.class})
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface I18nStringEntireNotEmpty {
  
  /**
   * Defines the default error message.
   */
  String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "i18nstringnotempty.message";
  
  /**
   * This contains groups.
   */
  Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[]payload() default {};
  
  /**
   * Defines several {@link I18nStringEntireNotEmpty} annotations on the same element.
   *
   * @see I18nStringEntireNotEmpty
   */
  @Target({ElementType.TYPE, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    I18nStringEntireNotEmpty[] value();
  }
}
