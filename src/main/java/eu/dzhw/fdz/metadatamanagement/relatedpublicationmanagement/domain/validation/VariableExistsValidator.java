package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.repository.VariableRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is an variable with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class VariableExistsValidator implements ConstraintValidator<VariableExists, String> {

  private final VariableRepository variableRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(VariableExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String variableId, ConstraintValidatorContext context) {   
    return variableRepository.existsById(variableId);
  }
}
