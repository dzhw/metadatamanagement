package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that a related publication is linked to the dataPackage of a DataSet.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ValidDataPackageIdsForDataSetIdsValidator
    implements ConstraintValidator<ValidDataPackageIdsForDataSetIds, RelatedPublication> {

  private final DataSetRepository dataSetRepository;

  private String messageKey;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidDataPackageIdsForDataSetIds constraintAnnotation) {
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
    if (relatedPublication.getDataPackageIds() == null
        || relatedPublication.getDataPackageIds().isEmpty()) {
      reportCustomViolation(relatedPublication, context, relatedPublication.getDataSetIds());
      return false;
    }

    List<String> invalidIds = relatedPublication.getDataSetIds().stream().filter(dataSetId -> {
      DataSet dataSet = dataSetRepository.findById(dataSetId).orElse(null);
      if (dataSet == null
          || relatedPublication.getDataPackageIds().contains(dataSet.getDataPackageId())) {
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
