package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.Survey;

/**
 * Validates the name of a id. The pattern is: DataAcquisitionProjectId-sy{Number}. This validator
 * validates the complete name.
 * 
 * @author dkatzberg
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
    if (survey.getDataAcquisitionProjectId() == null) {
      return false;
    }

    return survey.getId().matches(survey.getDataAcquisitionProjectId() + "\\-sy" + "[0-9]*");
  }

}
