package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;

/**
 * Validates the name of a variable.
 * 
 */
public class UniqueVariableIndexInDataSetValidator implements
    ConstraintValidator<UniqueVariableIndexInDataSet, Variable> {
  
  @Autowired
  private VariableRepository variableRepository;


  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueVariableIndexInDataSet constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    if (variable.getIndexInDataSet() == null || variable.getDataSetNumber() == null) {
      return true;
    }
    List<Variable> variables = variableRepository
        .findByIndexInDataSetAndDataSetId(variable.getIndexInDataSet(),
            variable.getDataAcquisitionProjectId() + "-ds" + variable.getDataSetNumber());
    if (variables.size() > 1) {
      return false;
    }
    if (variables.size() == 1) {
      return variables.get(0).getId().equals(variable.getId());
    }
    return true;
  }
}
