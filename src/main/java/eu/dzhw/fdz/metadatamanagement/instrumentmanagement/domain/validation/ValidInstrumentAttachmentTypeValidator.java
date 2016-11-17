package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentTypes;

/**
 * Validates the type of an {@link InstrumentAttachmentMetadata}.
 */
public class ValidInstrumentAttachmentTypeValidator
    implements ConstraintValidator<ValidInstrumentAttachmentType, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidInstrumentAttachmentType constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString type, ConstraintValidatorContext context) {
    return InstrumentAttachmentTypes.ALL.contains(type);
  }

}
