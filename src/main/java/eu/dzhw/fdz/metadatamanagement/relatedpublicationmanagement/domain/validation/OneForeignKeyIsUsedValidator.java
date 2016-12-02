package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;


/**
 * Checks for one list of foreign keys is filled.
 */
public class OneForeignKeyIsUsedValidator
    implements ConstraintValidator<OneForeignKeyIsUsed, RelatedPublication> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(OneForeignKeyIsUsed constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(RelatedPublication relatedPublication, 
      ConstraintValidatorContext context) {
    
    return !relatedPublication.getDataSetIds().isEmpty()
          || !relatedPublication.getInstrumentIds().isEmpty()
          || !relatedPublication.getQuestionIds().isEmpty()
          || !relatedPublication.getStudyIds().isEmpty()
          || !relatedPublication.getSurveyIds().isEmpty()
          || !relatedPublication.getVariableIds().isEmpty();
  }

}
