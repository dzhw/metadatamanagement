package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * The validator checks for an unique variable alias between all {@code VariableSurvey} objects with
 * the same surveyId.
 * 
 * @author Daniel Katzberg
 *
 */
public class UniqueVariableAliasValidator
    implements ConstraintValidator<UniqueVariableAlias, String> {
  
//  @Autowired
//  private VariableRepository variableRepository;

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
  public boolean isValid(String value, ConstraintValidatorContext context) {
//    variableRepository.matchFilterBySurveyId("", "");//TODO implement
    return true;
  }

}
