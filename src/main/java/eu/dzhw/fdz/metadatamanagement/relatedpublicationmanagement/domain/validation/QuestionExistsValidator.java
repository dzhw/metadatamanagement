package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a question with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class QuestionExistsValidator implements ConstraintValidator<QuestionExists, String> {

  private final QuestionRepository questionRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(QuestionExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String questionId, ConstraintValidatorContext context) {   
    return questionRepository.existsById(questionId);
  }
}
