package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;


/**
 * Checks for the study list. The list has to be filled.
 */
public class OneStudyOrStudySeriesIsUsedValidator
    implements ConstraintValidator<OneStudyOrStudySeriesIsUsed, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(OneStudyOrStudySeriesIsUsed constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    if (relatedPublication.getStudyIds() == null 
        && relatedPublication.getStudySerieses() == null) {
      return false;
    }
    if (relatedPublication.getStudyIds() == null) {
      return !relatedPublication.getStudySerieses().isEmpty();
    }
    if (relatedPublication.getStudySerieses() == null) {
      return !relatedPublication.getStudyIds().isEmpty();
    }
    
    return !relatedPublication.getStudyIds().isEmpty() 
        || !relatedPublication.getStudySerieses().isEmpty();
  }

}
