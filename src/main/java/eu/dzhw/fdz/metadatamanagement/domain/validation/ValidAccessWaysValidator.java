package eu.dzhw.fdz.metadatamanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.AccessWays;

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

    // check for empty list
    if (accessWays == null) {
      return false;
    }

    // min size = 1!
    if (accessWays.size() == 0) {
      return false;
    }

    // check all access ways
    for (String accessWay : accessWays) {
      switch (accessWay) {
        // All allowed values are okay. break switch.
        case AccessWays.CUF:
          break;
        case AccessWays.SUF:
          break;
        case AccessWays.REMOTE:
          break;
        // Not valid access way
        default:
          return false;
      }
    }

    // all values are okay
    return true;
  }

}
