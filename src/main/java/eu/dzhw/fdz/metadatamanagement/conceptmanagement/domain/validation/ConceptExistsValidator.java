package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is a concept with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class ConceptExistsValidator implements ConstraintValidator<ConceptExists, String> {

  private final ConceptRepository conceptRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ConceptExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String conceptId, ConstraintValidatorContext context) {   
    return conceptRepository.existsById(conceptId);
  }
}
