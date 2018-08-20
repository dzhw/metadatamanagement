package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.domain.Question;
import eu.dzhw.fdz.metadatamanagement.questionmanagement.repository.QuestionRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Ensure that a related publication is linked to the study of a question.
 * 
 * @author René Reitmann
 */
public class ValidStudyIdsForQuestionIdsValidator
    implements ConstraintValidator<ValidStudyIdsForQuestionIds, RelatedPublication> {

  @Autowired
  private QuestionRepository questionRepository;

  private String messageKey;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStudyIdsForQuestionIds constraintAnnotation) {
    messageKey = constraintAnnotation.message();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication,
      ConstraintValidatorContext context) {
    if (relatedPublication.getQuestionIds() == null
        || relatedPublication.getQuestionIds().isEmpty()) {
      return true;
    }
    if (relatedPublication.getStudyIds() == null || relatedPublication.getStudyIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getQuestionIds());
      return false;
    }

    List<String> invalidIds = relatedPublication.getQuestionIds().stream()
        .filter(questionId -> {
          Question question = questionRepository.findById(questionId).orElse(null);
          if (question == null 
              || relatedPublication.getStudyIds().contains(question.getStudyId())) {
            return false;
          }
          return true;
        }).collect(Collectors.toList());

    if (!invalidIds.isEmpty()) {
      reportCustomViolation(relatedPublication, context, invalidIds);
    }

    return invalidIds.isEmpty();
  }

  private void reportCustomViolation(RelatedPublication relatedPublication,
      ConstraintValidatorContext context, List<String> invalidIds) {
    context.disableDefaultConstraintViolation();
    invalidIds.forEach(invalidId -> {
      int questionIdIndex = relatedPublication.getQuestionIds().indexOf(invalidId);
      context.buildConstraintViolationWithTemplate(messageKey)
          .addPropertyNode("questionIds[" + questionIdIndex + "]").addConstraintViolation();
    });
  }
}
