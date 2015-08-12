package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * The unique variable alias annotation checks a variableAlias of the {@code VariableSurvey}. This
 * field must be unique between all variable surveys with the same surveyId.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {UniqueVariableAliasValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueVariableAlias {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.data."
      + "variablemanagement.documents.validation.uniquevariablealias.message}";

  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};

}
