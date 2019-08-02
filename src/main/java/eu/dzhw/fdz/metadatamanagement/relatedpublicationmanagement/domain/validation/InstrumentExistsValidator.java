package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;
import lombok.RequiredArgsConstructor;

/**
 * Validator which ensures that there is an instrument with the given id.
 * 
 * @author René Reitmann
 */
@RequiredArgsConstructor
public class InstrumentExistsValidator implements ConstraintValidator<InstrumentExists, String> {

  private final InstrumentRepository instrumentRepository;
  
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