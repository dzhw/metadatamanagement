package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a survey with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class SurveyExistsValidator implements ConstraintValidator<SurveyExists, String> {

  private final SurveyRepository surveyRepository;
  
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
    return surveyRepository.existsById(surveyId);
  }
}
