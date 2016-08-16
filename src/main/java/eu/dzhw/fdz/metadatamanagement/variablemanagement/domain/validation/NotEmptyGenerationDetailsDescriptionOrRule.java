package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation if a rule or description of the generation details is set. Both is
 * acceptable too.
 * 
 * @author dkatzberg
 *
 */
@Documented
@Constraint(validatedBy = {NotEmptyGenerationDetailsDescriptionOrRuleValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmptyGenerationDetailsDescriptionOrRule {
  /**
   * Defines the default error message.
   */
  public abstract String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "not-empty-generation-details-description-or-rule.message";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
}
