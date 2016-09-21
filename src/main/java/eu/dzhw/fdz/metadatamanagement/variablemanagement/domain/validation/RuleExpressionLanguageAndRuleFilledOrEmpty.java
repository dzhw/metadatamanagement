package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the validation of the rule expression language field. If a the rule field is set
 * in the generation details, the rule expression language have to be non empty.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {RuleExpressionLanguageAndRuleFilledOrEmptyValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RuleExpressionLanguageAndRuleFilledOrEmpty {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "mandatoryExpressionLanguageRuleIfRuleExist.message";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};
  
}
