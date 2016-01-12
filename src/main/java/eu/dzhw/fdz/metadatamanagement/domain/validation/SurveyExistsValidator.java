package eu.dzhw.fdz.metadatamanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.repository.SurveyRepository;

/**
 * Validate that a survey with the given id exists.
 * @author René Reitmann
 */
public class SurveyExistsValidator implements ConstraintValidator<SurveyExists, String> {

  @Autowired
  private SurveyRepository surveyRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(SurveyExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String surveyId, ConstraintValidatorContext context) {
    
    // Empty survey id is ok
    if (StringUtils.isEmpty(surveyId)) {
      return true;
    }

    return this.surveyRepository.exists(surveyId);
  }

}
