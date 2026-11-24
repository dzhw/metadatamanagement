package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentTypes;

/**
 * Validates the type of an {@link Instrument}.
 */
public class ValidInstrumentTypeValidator
    implements ConstraintValidator<ValidInstrumentType, String> {

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidInstrumentType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String type, ConstraintValidatorContext context) {
    return InstrumentTypes.ALL.contains(type);
  }

}
