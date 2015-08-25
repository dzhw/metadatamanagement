package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annoation is for an id field. It checks the id for no change in an edit process of the
 * variable document.
 * 
 * @author Daniel Katzberg
 */
@Documented
@Constraint(validatedBy = {NoEditableIdValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoEditableId {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.data."
      + "variablemanagement.documents.validation.noeditableid.message}";

  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};

}
