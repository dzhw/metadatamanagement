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
    
    //boolean vars 
    boolean notEmptyDataSetIds = false;
    boolean notEmptyInstrumentIds = false;
    boolean notEmptyQuestionIds = false;
    boolean notEmptyStudyIds = false;
    boolean notEmptySurveyIds = false;
    boolean notEmptyVariableIds = false;
    
    
    //check all lists for null and set the isEmpty Value
    if (relatedPublication.getDataSetIds() != null) {
      notEmptyDataSetIds = !relatedPublication.getDataSetIds().isEmpty();
    }
    if (relatedPublication.getInstrumentIds() != null) {
      notEmptyInstrumentIds = !relatedPublication.getInstrumentIds().isEmpty();
    }
    if (relatedPublication.getQuestionIds() != null) {
      notEmptyQuestionIds = !relatedPublication.getQuestionIds().isEmpty();
    }
    if (relatedPublication.getStudyIds() != null) {
      notEmptyStudyIds = !relatedPublication.getStudyIds().isEmpty();
    }
    if (relatedPublication.getSurveyIds() != null) {
      notEmptySurveyIds = !relatedPublication.getSurveyIds().isEmpty();
    }
    if (relatedPublication.getVariableIds() != null) {
      notEmptyVariableIds = !relatedPublication.getVariableIds().isEmpty();
    }
       
    
    return notEmptyDataSetIds || notEmptyInstrumentIds || notEmptyQuestionIds || notEmptyStudyIds
          || notEmptySurveyIds || notEmptyVariableIds;
  }

}
