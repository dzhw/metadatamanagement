package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * AnalysisPackageIds must be equal to "ana-{{project id}}$".
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {ValidAnalysisPackageIdValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAnalysisPackageId {

  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.analyispackagemanagement"
      + ".domain.validation.validAnalysisPackageId.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

}
