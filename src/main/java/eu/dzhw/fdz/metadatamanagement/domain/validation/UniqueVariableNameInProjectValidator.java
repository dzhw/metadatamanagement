package eu.dzhw.fdz.metadatamanagement.domain.validation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.repository.VariableRepository;

/**
 * This validator checks a name of a variable within a project. The name has to be unique within the
 * project.
 * 
 * @author Daniel Katzberg
 *
 */
public class UniqueVariableNameInProjectValidator
    implements ConstraintValidator<UniqueVariableNameInProject, Variable> {

  @Inject
  private VariableRepository variableRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueVariableNameInProject constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {

    // search for variables with the same project id and name (not id!)
    List<Variable> variablesWithSameNameAndProject =
        this.variableRepository.findByDataAcquisitionProjectIdAndName(
        variable.getDataAcquisitionProjectId(), variable.getName());
    
    // ignore the same object (for an update)
    List<Variable> variablesWithoutSameVariable = new ArrayList<>();
    for (Variable variableFromList : variablesWithSameNameAndProject) {
      if (!variableFromList.getId()
          .equals(variable.getId())) {
        variablesWithoutSameVariable.add(variableFromList);
      }
    }
    
    //check for variables with same name and project id
    // if no other found .-> good!
    return variablesWithoutSameVariable.size() == 0;
  }

}
