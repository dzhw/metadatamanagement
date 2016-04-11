package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Value;

/**
 * Ensure that value.code is unique within the variable.
 * 
 * @author René Reitmann
 */
public class UniqueValueCodeValidator
    implements ConstraintValidator<UniqueValueCode, List<Value>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueValueCode constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<Value> values, ConstraintValidatorContext context) {
    if (values == null) {
      return true;
    }
    
    HashSet<Integer> codes = new HashSet<>();
    
    for (Value value : values) {
      if (value.getCode() != null) {
        if (codes.contains(value.getCode())) {
          return false;
        }
        codes.add(value.getCode());        
      }
    }
    
    return true;
  }
}
