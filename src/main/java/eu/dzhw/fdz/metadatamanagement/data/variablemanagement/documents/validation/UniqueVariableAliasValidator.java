package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableSurvey;
import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.repositories.VariableRepository;

/**
 * The validator checks for an unique variable alias between all {@code VariableSurvey} objects with
 * the same surveyId.
 * 
 * @author Daniel Katzberg
 *
 */
public class UniqueVariableAliasValidator
    implements ConstraintValidator<UniqueVariableAlias, VariableSurvey> {

  @Autowired
  private VariableRepository variableRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueVariableAlias constraintAnnotation) {
    // Do nothing
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(VariableSurvey variableSurvey, ConstraintValidatorContext context) {

    // A check for null is not necessary, because it is a type annotation. it can only be checked by
    // initialized objects

    // Both are not empty fields -> no valid if there are null
    if (variableSurvey.getSurveyId() == null || variableSurvey.getVariableAlias() == null) {
      return false;
    }

    // no elements found
    if (variableRepository.filterBySurveyIdAndVariableAlias(variableSurvey.getSurveyId(),
        variableSurvey.getVariableAlias()).getNumberOfElements() == 0) {
      return true;
      // found elements
    } else {
      return false;
    }
  }
}
