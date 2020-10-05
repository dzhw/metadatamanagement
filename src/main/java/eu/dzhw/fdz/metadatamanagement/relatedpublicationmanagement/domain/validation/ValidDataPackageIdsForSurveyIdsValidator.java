package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that a related publication is linked to the dataPackage of a survey.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ValidDataPackageIdsForSurveyIdsValidator
    implements ConstraintValidator<ValidDataPackageIdsForSurveyIds, RelatedPublication> {

  private final SurveyRepository surveyRepository;

  private String messageKey;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageIdsForSurveyIds constraintAnnotation) {
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
    if (relatedPublication.getSurveyIds() == null || relatedPublication.getSurveyIds().isEmpty()) {
      return true;
    }
    if (relatedPublication.getDataPackageIds() == null
        || relatedPublication.getDataPackageIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getSurveyIds());
      return false;
    }

    List<String> invalidIds = relatedPublication.getSurveyIds().stream().filter(surveyId -> {
      Survey survey = surveyRepository.findById(surveyId).orElse(null);
      if (survey == null
          || relatedPublication.getDataPackageIds().contains(survey.getDataPackageId())) {
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
      int surveyIdIndex = relatedPublication.getSurveyIds().indexOf(invalidId);
      context.buildConstraintViolationWithTemplate(messageKey)
          .addPropertyNode("surveyIds[" + surveyIdIndex + "]").addConstraintViolation();
    });
  }
}
