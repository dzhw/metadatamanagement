package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *  Ensure that numeric variables have a scaleLevel.
 *  
 *  @author René Reitmann
 */
@Documented
@Constraint(validatedBy = {MandatoryScaleLevelForNumericDataTypeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MandatoryScaleLevelForNumericDataType {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain.validation."
      + "mandatoryScaleLevelForNumericDataType.message}";

  /**
   * This contains groups.
   */
  public Class<?>[] groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[] payload() default {};

}
