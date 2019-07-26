package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validates the uniqueness of question.
 */
@RequiredArgsConstructor
public class ValidUniqueQuestionNumberValidator
    implements ConstraintValidator<ValidUniqueQuestionNumber, Question> {
  
  private final QuestionRepository questionRepository;

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
    if (question.isShadow()) {
      return true;
    } else {
      if (question.getInstrumentId() != null
          && question.getNumber() != null) {

        List<IdAndVersionProjection> existingQuestions =
            questionRepository.findIdsByInstrumentIdAndNumber(question.getInstrumentId(),
                question.getNumber());
        if (existingQuestions.isEmpty()) {
          return true;
        } else {
          //we are updating this question
          return existingQuestions.get(0).getId().equals(question.getId());
        }
      }
      return true;
    }

  }
}
