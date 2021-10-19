package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Checks that there is at most one publication per analysis package.
 */
@Documented
@Constraint(validatedBy = {AtMostOnePublicationPerAnalysisPackageValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtMostOnePublicationPerAnalysisPackage {

  /**
   * Defines the default error message.
   */
  String message() default "related-publication-management.error."
      + "related-publication.more-than-one-publication-per-analysis-package";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
