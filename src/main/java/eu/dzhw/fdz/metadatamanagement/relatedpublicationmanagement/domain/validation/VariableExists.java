package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation.VariableExists.List;

/**
 * Checks that the referenced variable exists.
 */
@Documented
@Constraint(validatedBy = {VariableExistsValidator.class})
@Repeatable(List.class)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VariableExists {

  /**
   * Defines the default error message.
   */
  String message() default "related-publication-management.error."
      + "related-publication.variable-exists";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Defines several {@link VariableExists} annotations on the same element.
   *
   * @see VariableExists
   */
  @Target({ElementType.TYPE, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    VariableExists[] value();
  }
}
