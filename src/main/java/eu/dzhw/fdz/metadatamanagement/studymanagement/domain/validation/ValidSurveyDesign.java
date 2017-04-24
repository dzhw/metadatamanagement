package eu.dzhw.fdz.metadatamanagement.studymanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDesigns;

/**
 * SurveyDesign is limited to some values, which are define by the 
 * class {@link SurveyDesigns}. This is the annotation for the validation of the valid values.
 * 
 * @author Daniel Katzberg
 */
@Documented
@Constraint(validatedBy = {ValidSurveyDesignValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSurveyDesign {
  
  /**
   * Defines the default error message.
   */
  String message() default "{eu.dzhw.fdz.metadatamanagement.studymanagement" 
   + ".domain.validation.validSurveyDesigns.message}";

  /**
   * This contains groups.
   */
  Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  Class<? extends Payload>[] payload() default {};

}
