package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import lombok.RequiredArgsConstructor;

/**
 * Ensure that there is at least one dataPackageId or at least one analysisPackageId.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class DataPackageIdsOrAnalysisPackageIdsPresentValidator implements
    ConstraintValidator<DataPackageIdsOrAnalysisPackageIdsPresent, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(DataPackageIdsOrAnalysisPackageIdsPresent constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication,
      ConstraintValidatorContext context) {
    if ((relatedPublication.getAnalysisPackageIds() == null
        || relatedPublication.getAnalysisPackageIds().isEmpty())
        && (relatedPublication.getDataPackageIds() == null
            || relatedPublication.getDataPackageIds().isEmpty())) {
      return false;
    }
    return true;
  }
}
