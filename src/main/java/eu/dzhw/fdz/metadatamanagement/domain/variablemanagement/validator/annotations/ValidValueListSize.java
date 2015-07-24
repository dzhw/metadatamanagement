package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.Value;
import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.ValueListSizeValidator;

/**
 * This annotation compares the size of two list of the {@link Value} class. The list
 * value {@code Value.getValues()} has to be bigger than the list of value lables
 * {@code Value.getValueLables()}
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {ValueListSizeValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidValueListSize {

  /**
   * Defines the default error message.
   */
  public abstract String message() default "{eu.dzhw.fdz.metadatamanagement.domain."
  + "variablemanagement.validator.annotations.ValidValueListSize.message}";

}
