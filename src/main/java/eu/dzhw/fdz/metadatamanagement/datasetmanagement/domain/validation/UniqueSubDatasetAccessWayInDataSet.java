package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation is for the validator, which checks the accesways of subdataset within a project. 
 * The sub data set accessways has to be unique within the data set.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {UniqueSubDataSetAccessWayInDataSetValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueSubDatasetAccessWayInDataSet {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "uniqueSubDatasetAccessWayInDataSet.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
