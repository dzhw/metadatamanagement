package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.AccessWays;

/**
 * Validator for the access ways of a variable. Only valued from the {@link AccessWays} class are
 * allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidAccessWaysValidator
    implements ConstraintValidator<ValidAccessWays, List<String>> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidAccessWays constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(List<String> accessWays, ConstraintValidatorContext context) {

    // min size 1!
    if (accessWays == null || accessWays.size() == 0) {
      return false;
    }

    // check for access ways
    for (String accessWay : accessWays) {
      // check for every access way. if one way is not valid, return false
      if (!AccessWays.ALL.contains(accessWay)) {
        return false;
      }
    }

    // all access ways are valid.
    return true;
  }

}
