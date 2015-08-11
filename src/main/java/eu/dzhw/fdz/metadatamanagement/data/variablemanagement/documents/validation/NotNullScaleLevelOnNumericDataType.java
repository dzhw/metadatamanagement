package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * This annotation checks for a not null scale level, if a data type is numeric.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {NotNullScaleLevelOnNumericDataTypeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullScaleLevelOnNumericDataType {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.data."
      + "variablemanagement.documents.validation.notnullscalelevelonnumericdatatype.message}";

  /**
   * This contains groups.
   */
  public Class<?>[]groups() default {};

  /**
   * This method contains the payload.
   */
  public Class<? extends Payload>[]payload() default {};

}
