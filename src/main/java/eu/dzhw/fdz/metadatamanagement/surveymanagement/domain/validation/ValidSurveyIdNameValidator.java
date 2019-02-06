package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Validates the name of a id. The pattern is: sur-{DataAcquisitionProjectId}-sy{Number}.
 * This validator validates the complete name.
 *
 * In case of a shadow copy the name must end with a version suffix (e. g. -1.0.0)
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidSurveyIdNameValidator implements ConstraintValidator<ValidSurveyIdName, Survey> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidSurveyIdName constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Survey survey, ConstraintValidatorContext context) {
    // check for set project id
    if (survey.getId() == null || survey.getDataAcquisitionProjectId() == null
        || survey.getNumber() == null) {
      return false;
    }

    if (survey.isShadow()) {
      return survey.getId().matches(createValidShadowCopyIdPattern(survey));
    } else {
      return survey.getId().equals(createValidIdValue(survey));
    }
  }

  private static String createValidShadowCopyIdPattern(Survey survey) {
    return "sur-" + survey.getDataAcquisitionProjectId() + "-sy" + survey.getNumber()
        + "\\$-[0-9]+\\.[0-9]+\\.[0-9]+";
  }

  private static String createValidIdValue(Survey survey) {
    return "sur-" + survey.getDataAcquisitionProjectId() + "-sy" + survey.getNumber() + "$";
  }

}
