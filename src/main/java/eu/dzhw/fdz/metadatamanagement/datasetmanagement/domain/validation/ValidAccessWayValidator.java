package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;

/**
 * Validator for the access way of a subDataSet. Only valued from the {@link AccessWays} class are
 * allowed.
 */
public class ValidAccessWayValidator
    implements ConstraintValidator<ValidAccessWay, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAccessWay constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String accessWay, ConstraintValidatorContext context) {
    
    // check for access way
    if (!AccessWays.ALL.contains(accessWay) || accessWay
          .equals(AccessWays.NOT_ACCESSIBLE)) {
      return false;
    }
      
    return true;
  }

}
