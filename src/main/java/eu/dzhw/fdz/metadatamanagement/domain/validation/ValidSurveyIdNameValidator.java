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

    String[] splittedId = survey.getId()
      .split("-sy");

    // The length has to be 2. Before the minus has to be the project id and after the minus has to
    // be the variable name.
    if (splittedId.length != 2) {
      return false;
    }


    // Check for Number
    try {
      Integer.valueOf(splittedId[1]);
    } catch (NumberFormatException e) {
      // No number!
      return false;
    }

    // check for correct project id and variable name
    if (survey.getDataAcquisitionProjectId()
        .equals(splittedId[0])) {
      return true;
    }

    // the project id or the name is in the id not valid.
    return false;
  }

}
