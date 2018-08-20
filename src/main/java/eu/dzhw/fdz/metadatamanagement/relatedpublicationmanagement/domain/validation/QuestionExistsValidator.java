package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;

/**
 * Validator which ensures that there is a question with the given id.
 * 
 * @author René Reitmann
 */
public class QuestionExistsValidator implements ConstraintValidator<QuestionExists, String> {

  @Autowired
  private QuestionRepository questionRepository;
  
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
