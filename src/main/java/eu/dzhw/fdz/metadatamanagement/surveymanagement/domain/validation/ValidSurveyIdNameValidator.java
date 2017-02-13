package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;

/**
 * Validates the name of a id. The pattern is: DataAcquisitionProjectId-sy{Number}. This validator
 * validates the complete name.
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

    return survey.getId().equals("sur-" + survey.getDataAcquisitionProjectId()
        + "-sy" + survey.getNumber() + "!");
  }

}
