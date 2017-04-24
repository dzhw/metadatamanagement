package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.ScaleLevels;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;

/**
 * Ensure that there is enough information to display a histogram
 * for ratio variables.
 */
public class MandatoryHistogramOnRatioScaleLevelValidator
    implements ConstraintValidator<MandatoryHistogramOnRatioScaleLevel, Variable> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(MandatoryHistogramOnRatioScaleLevel constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    // only validate ratio scaled variables
    if (variable.getScaleLevel() == null || !variable.getScaleLevel().equals(ScaleLevels.RATIO)) {
      return true;
    }
    if (variable.getDistribution() == null || variable.getDistribution().getHistogram() == null
        || variable.getDistribution().getHistogram().getNumberOfBins() == null
        || variable.getDistribution().getHistogram().getStart() == null
        || variable.getDistribution().getHistogram().getEnd() == null) {      
      return false;
    }
    return true;
  }
}
