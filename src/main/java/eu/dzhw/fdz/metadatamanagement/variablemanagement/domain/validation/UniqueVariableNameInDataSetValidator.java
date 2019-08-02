package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.projections.IdAndVersionProjection;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validates the name of a variable.
 * 
 */
@RequiredArgsConstructor
public class UniqueVariableNameInDataSetValidator implements
    ConstraintValidator<UniqueVariableNameInDataSet, Variable> {
  
  private final VariableRepository variableRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(UniqueVariableNameInDataSet constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Variable variable, ConstraintValidatorContext context) {
    if (variable.isShadow()) {
      return true;
    } else {
      if (StringUtils.isEmpty(variable.getName()) && StringUtils.isEmpty(variable.getDataSetId())) {
        return false;
      }
      List<IdAndVersionProjection> variables = variableRepository
          .findIdsByNameAndDataSetId(variable.getName(), variable.getDataSetId());
      if (variables.size() > 1) {
        return false;
      }
      if (variables.size() == 1) {
        return variables.get(0).getId().equals(variable.getId());
      }
      return true;
    }
  }
}
