package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;

/**
 * Validates the uniqueness of question.
 */
public class ValidUniqueQuestionNumberValidator
    implements ConstraintValidator<ValidUniqueQuestionNumber, Question> {
  
  @Autowired
  private QuestionRepository questionRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidUniqueQuestionNumber constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Question question, ConstraintValidatorContext context) {
    if (question.getInstrumentId() != null
        && question.getNumber() != null) {
        
      List<IdAndVersionProjection> existingQuestions = 
          questionRepository.findIdsByInstrumentIdAndNumber(question.getInstrumentId(),
          question.getNumber());
      if (existingQuestions.isEmpty()) {
        return true;
      } else {
        if (existingQuestions.get(0).getId().equals(question.getId())) {
          //we are updating this question
          return true;
        } else {
          return false;
        }
      }
    }
    return true;
  }
}
