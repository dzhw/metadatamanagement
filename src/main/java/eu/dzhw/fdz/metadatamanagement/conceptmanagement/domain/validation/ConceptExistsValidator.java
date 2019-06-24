package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.repository.ConceptRepository;

/**
 * Validator which ensures that there is a concept with the given id.
 * 
 * @author René Reitmann
 */
public class ConceptExistsValidator implements ConstraintValidator<ConceptExists, String> {

  @Autowired
  private ConceptRepository conceptRepository;
  
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
