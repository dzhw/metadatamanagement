package eu.dzhw.fdz.metadatamanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotion is for the validation of the dependency between variable, survey and fdz project.
 * It should be validate, if the a optional survey is part of the same fdz project.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValidSurveyFdzProjectRelationValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSurveyFdzProjectRelation {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "validsurveyfdzprojectrelation.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};

}
