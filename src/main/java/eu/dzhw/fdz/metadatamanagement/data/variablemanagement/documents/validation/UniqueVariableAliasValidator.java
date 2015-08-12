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
  public boolean isValid(VariableSurvey value, ConstraintValidatorContext context) {

    if (value.getSurveyId() == null || value.getVariableAlias() == null) {
      return true;
    }

    // no elements found
    if (variableRepository.matchFilterBySurveyId(value.getSurveyId(), value.getVariableAlias())
        .getNumberOfElements() == 0) {
      return true;
      // found elements
    } else {
      return false;
    }
  }
}
