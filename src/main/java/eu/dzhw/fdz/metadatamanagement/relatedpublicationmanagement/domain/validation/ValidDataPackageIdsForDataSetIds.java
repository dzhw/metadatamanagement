package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * When a data set is linked the dataPackage must be linked as well.
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {ValidDataPackageIdsForDataSetIdsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDataPackageIdsForDataSetIds {
  
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement" 
   + ".domain.validation.validDataPackageIdsForDataSetIds.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

}
