package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that a related publication is linked to the study of a variable.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ValidStudyIdsForVariableIdsValidator
    implements ConstraintValidator<ValidStudyIdsForVariableIds, RelatedPublication> {

  private final VariableRepository variableRepository;

  private String messageKey;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStudyIdsForVariableIds constraintAnnotation) {
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
    if (relatedPublication.getVariableIds() == null
        || relatedPublication.getVariableIds().isEmpty()) {
      return true;
    }
    if (relatedPublication.getStudyIds() == null || relatedPublication.getStudyIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getVariableIds());
      return false;
    }

    List<String> invalidIds = relatedPublication.getVariableIds().stream()
        .filter(variableId -> {
          Variable variable = variableRepository.findById(variableId).orElse(null);
          if (variable == null 
              || relatedPublication.getStudyIds().contains(variable.getStudyId())) {
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
      int variableIdIndex = relatedPublication.getVariableIds().indexOf(invalidId);
      context.buildConstraintViolationWithTemplate(messageKey)
          .addPropertyNode("variableIds[" + variableIdIndex + "]").addConstraintViolation();
    });
  }
}
