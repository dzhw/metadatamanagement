package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Ensure that there is at least one dataPackageId or at least one analysisPackageId.
 */
@Documented
@Constraint(validatedBy = {DataPackageIdsOrAnalysisPackageIdsPresentValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPackageIdsOrAnalysisPackageIdsPresent {

  /**
   * Defines the default error message.
   */
  String message() default "related-publication-management.error."
      + "related-publication.at-least-one-referenced-id";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
