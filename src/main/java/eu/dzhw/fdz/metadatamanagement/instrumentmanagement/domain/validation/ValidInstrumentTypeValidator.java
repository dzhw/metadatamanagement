package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentTypes;

/**
 * Validates the name of a id.
 */
public class ValidInstrumentTypeValidator
    implements ConstraintValidator<ValidInstrumentType, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidInstrumentType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String type, ConstraintValidatorContext context) {
    return InstrumentTypes.ALL.contains(type);
  }

}
