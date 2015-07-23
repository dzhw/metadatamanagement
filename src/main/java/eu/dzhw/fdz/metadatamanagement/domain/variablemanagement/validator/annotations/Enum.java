package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.EnumValidator;

/**
 * This is the validation annotation for a String which should is part of a enumeration.
 * 
 * @author Daniel Katzberg
 *
 */
@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {

  /**
   * This is the error message if the validation does not accept a value.
   */
  public abstract String message() default "Not a accepted value.";// TODO

  /**
   * This is parameter for getting a enumeration class for the validation.
   */
  public abstract Class<? extends java.lang.Enum<?>> enumClass();
}
