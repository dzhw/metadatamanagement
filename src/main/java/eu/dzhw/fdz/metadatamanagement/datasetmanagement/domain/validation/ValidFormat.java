package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.Format;

/**
 * Annotation for the validator of the format of the data sets. Only values from the class 
 * {@link Format} are allowed.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidFormatValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFormat {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validformat.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
