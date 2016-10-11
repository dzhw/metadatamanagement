package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSetTypes;

/**
 * Annotation for the validator for the data set type. 
 * Only values of the {@link DataSetTypes} Strings are allowed.
 *
 */
@Documented
@Constraint(validatedBy = {ValidDataSetTypeValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDataSetType {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validdatasettype.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
