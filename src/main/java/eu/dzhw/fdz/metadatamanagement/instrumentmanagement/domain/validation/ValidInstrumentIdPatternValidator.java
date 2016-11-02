package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.Instrument;

/**
 * Validates the name of a id.
 */
public class ValidInstrumentIdPatternValidator
    implements ConstraintValidator<ValidInstrumentIdPattern, Instrument> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidInstrumentIdPattern constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(Instrument instrument, ConstraintValidatorContext context) {

    // check precondition
    if (instrument.getDataAcquisitionProjectId() == null || instrument.getId() == null) {
      return false;
    }
    if (instrument.getId().startsWith(instrument.getDataAcquisitionProjectId() + "-ins")) {
      try {
        Integer.parseInt(instrument.getId().replace(
            instrument.getDataAcquisitionProjectId() + "-ins", ""));
        return true;
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return false;
  }

}
