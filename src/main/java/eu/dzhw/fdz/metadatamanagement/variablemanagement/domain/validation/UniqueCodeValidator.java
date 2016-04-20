package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Missing;

/**
 * Ensure that missing.code is unique within the variable.
 * 
 * @author René Reitmann
 */
public class UniqueCodeValidator
    implements ConstraintValidator<UniqueCode, List<Missing>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueCode constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<Missing> missings, ConstraintValidatorContext context) {
    if (missings == null) {
      return true;
    }
    
    HashSet<Integer> codes = new HashSet<>();
    
    for (Missing value : missings) {
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
