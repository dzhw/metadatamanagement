package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the consistence of given types. 
 * Is english 'open' and german 'offen' set at same time? If it is not, 
 * the validation going be wrong.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidAtomicQuestionTypeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAtomicQuestionType {
  
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validatomicquestiontype.message}";
  
  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};
  
  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};

}
