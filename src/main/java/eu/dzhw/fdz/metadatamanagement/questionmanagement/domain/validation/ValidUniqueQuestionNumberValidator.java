package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import java.util.List;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;

/**
 * Validates the uniqueness of question.
 */
public class ValidUniqueQuestionNumberValidator
    implements ConstraintValidator<ValidUniqueQuestionNumber, Question> {
  
  @Inject
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
        && question.getNumber() != null
        && !toBeUpdated(questionRepository
            .findByInstrumentIdAndNumber(question.getInstrumentId(),
                question.getNumber()), question)) {
      return false;
    }
    return true;
  }
  
  /**
   * For question update is redundant of questions allowed .
   * @param questions the list of questions
   * @param question the new object
   * @return result of check
   */
  public boolean toBeUpdated(List<Question> questions, Question question ) {
    if (questions.size() == 1) {
      if (questions.get(0).getId().equals(question.getId())) {
        return true;
      } else {
        return false;
      }
    }
    return true;
  }
}
