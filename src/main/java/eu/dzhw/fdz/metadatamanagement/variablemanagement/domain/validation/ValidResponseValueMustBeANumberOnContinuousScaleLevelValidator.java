package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ValidResponse;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Validates the valid responses of a variable. It checks the values has a numeric string, if the
 * variable has a continuous scale level.
 * 
 * @author dkatzberg
 *
 */
public class ValidResponseValueMustBeANumberOnContinuousScaleLevelValidator implements
    ConstraintValidator<ValidResponseValueMustBeANumberOnContinuousScaleLevel, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(
      ValidResponseValueMustBeANumberOnContinuousScaleLevel constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    if (variable == null) {
      return true;
    }

    if (variable.getScaleLevel() == null) {
      return true;
    }

    if (variable.getScaleLevel().equals(ScaleLevels.CONTINOUS)) {
      String regex = "-?\\d+(\\.\\d+)?";
      for (ValidResponse validResponse : variable.getDistribution().getValidResponses()) {
        // if one value is not numeric ... send a false.
        if (!validResponse.getValue().matches(regex)) {
          return false;
        }
      }
    }

    // no numeric, everything is okay.
    return true;
  }

}
