package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;

/**
 * Validates the uniqueness of number.
 */
public class ValidUniqueSurveyNumberValidator
    implements ConstraintValidator<ValidUniqueSurveyNumber, Survey> {
  
  @Autowired
  private SurveyRepository surveyRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidUniqueSurveyNumber constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Survey survey, ConstraintValidatorContext context) {
    if (survey.getNumber() != null) {
      Long count = surveyRepository
          .countByNumberAndDataAcquisitionProjectId(survey
              .getNumber(), survey.getDataAcquisitionProjectId());
      if (count > 0) {
        return false; 
      }
    }
    return true;
  }
}
