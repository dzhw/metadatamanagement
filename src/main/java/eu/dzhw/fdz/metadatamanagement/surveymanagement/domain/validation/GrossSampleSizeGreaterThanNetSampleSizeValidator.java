package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 *  Validate that {@link Survey}.grossSampleSize is greater than or equal to
 * {@link Survey}.sampleSize.
 */
public class GrossSampleSizeGreaterThanNetSampleSizeValidator
    implements ConstraintValidator<GrossSampleSizeGreaterThanNetSampleSize, Survey> {

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(GrossSampleSizeGreaterThanNetSampleSize constraintAnnotation) {}

  /*
   * (non-Javadoc)
   *
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Survey survey, ConstraintValidatorContext context) {
    if (survey.getGrossSampleSize() == null || survey.getSampleSize() == null) {
      return true;
    }
    return survey.getGrossSampleSize() >= survey.getSampleSize();
  }
}
