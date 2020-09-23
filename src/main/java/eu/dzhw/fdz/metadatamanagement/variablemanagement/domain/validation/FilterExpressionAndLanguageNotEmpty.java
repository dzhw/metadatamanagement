package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Ensure that either both of filterDetails.expressionLanguage and filterDetails.expression are set
 * or none of them.
 * 
 * @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {FilterExpressionAndLanguageNotEmptyValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterExpressionAndLanguageNotEmpty {
  /**
   * Defines the default error message.
   */
  String message() default "variable-management.error.filter-details."
      + "expression.both-not-empty";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};
}
