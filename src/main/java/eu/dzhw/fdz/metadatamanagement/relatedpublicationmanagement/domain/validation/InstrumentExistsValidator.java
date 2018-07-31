package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;

/**
 * Validator which ensures that there is an instrument with the given id.
 * 
 * @author René Reitmann
 */
public class InstrumentExistsValidator implements ConstraintValidator<InstrumentExists, String> {

  @Autowired
  private InstrumentRepository instrumentRepository;
  
  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(InstrumentExists constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String instrumentId, ConstraintValidatorContext context) {   
    return instrumentRepository.existsById(instrumentId);
  }
}