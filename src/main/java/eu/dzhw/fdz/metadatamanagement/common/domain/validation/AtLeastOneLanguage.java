package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Ensure that at least one I18nString attribute is given.
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {AtLeastOneLanguageValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneLanguage {
  
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "atleastonelanguage.message}";
  
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};
}
