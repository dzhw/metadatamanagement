package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.validation;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;

/**
 * Validates the language of an {@link InstrumentAttachmentMetadata}.
 */
public class ValidIsoLanguageValidator
    implements ConstraintValidator<ValidIsoLanguage, String> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidIsoLanguage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String language, ConstraintValidatorContext context) {
    return Arrays.asList(Locale.getISOLanguages()).contains(language);
  }

}
