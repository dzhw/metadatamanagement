package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for the Validation for Value. If the value is no missing, the relative frequency is
 * mandatory.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {NotNullValidRelativeFrequencyIfNoMissingValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullValidRelativeFrequencyIfNoMissing {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.variablemanagement"
      + "domain.validation.mandatoryScaleLevel.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};

}
