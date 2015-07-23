/**
 * 
 */
package eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import eu.dzhw.fdz.metadatamanagement.domain.variablemanagement.validator.EnumValidator;

@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/**
 * This is the validation annotation for a String which should is part of a enumeration.
 * 
 * @author Daniel Katzberg
 *
 */
public @interface Enum {

  public abstract String message() default "Not a accepted value.";// TODO

  public abstract Class<? extends java.lang.Enum<?>>enumClass();
}
