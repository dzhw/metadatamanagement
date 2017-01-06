package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.repository.InstrumentRepository;

/**
 * Validates the uniqueness of question.
 */
public class ValidUniqueInstrumentNumberValidator
    implements ConstraintValidator<ValidUniqueInstrumentNumber, Instrument> {
  
  @Inject
  private InstrumentRepository instrumentRepository;

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidUniqueInstrumentNumber constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Instrument instrument, ConstraintValidatorContext context) {
    if (instrument.getNumber() != null) {
      Long count = instrumentRepository
          .countByNumberAndDataAcquisitionProjectId(instrument
              .getNumber(), instrument.getDataAcquisitionProjectId());
      if (count > 0) {
        return false; 
      }
    }
    return true;
  }
}
