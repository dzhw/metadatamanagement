package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation.ConceptExists.List;

/**
 * Checks that the referenced concept exists.
 */
@Documented
@Constraint(validatedBy = {ConceptExistsValidator.class})
@Repeatable(List.class)
@Target({ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConceptExists {

  /**
   * Defines the default error message.
   */
  String message() default "concept-management.error."
      + "concept.concept-exists";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

  /**
   * Defines several {@link ConceptExists} annotations on the same element.
   *
   * @see ConceptExists
   */
  @Target({ElementType.TYPE, ElementType.TYPE_USE})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @interface List {
    ConceptExists[] value();
  }
}
