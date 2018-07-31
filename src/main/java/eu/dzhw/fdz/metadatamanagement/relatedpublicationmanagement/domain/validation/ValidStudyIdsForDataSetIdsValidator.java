package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;

/**
 * Ensure that a related publication is linked to the study of a DataSet.
 * 
 * @author René Reitmann
 */
public class ValidStudyIdsForDataSetIdsValidator
    implements ConstraintValidator<ValidStudyIdsForDataSetIds, RelatedPublication> {

  @Autowired
  private DataSetRepository dataSetRepository;

  private String messageKey;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidStudyIdsForDataSetIds constraintAnnotation) {
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
    if (relatedPublication.getDataSetIds() == null
        || relatedPublication.getDataSetIds().isEmpty()) {
      return true;
    }
    if (relatedPublication.getStudyIds() == null || relatedPublication.getStudyIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getDataSetIds());
      return false;
    }

    List<String> invalidIds = relatedPublication.getDataSetIds().stream()
        .filter(dataSetId -> {
          DataSet dataSet = dataSetRepository.findById(dataSetId).orElse(null);
          if (dataSet == null || relatedPublication.getStudyIds().contains(dataSet.getStudyId())) {
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
      int dataSetIdIndex = relatedPublication.getDataSetIds().indexOf(invalidId);
      context.buildConstraintViolationWithTemplate(messageKey)
          .addPropertyNode("dataSetIds[" + dataSetIdIndex + "]").addConstraintViolation();
    });
  }
}
