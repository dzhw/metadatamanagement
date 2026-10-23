package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates the language to be an ISO 639 language code.
 */
public class ValidIsoLanguageValidator
    implements ConstraintValidator<ValidIsoLanguage, String> {

  private static final List<String> ISO_LANGUAGES = Arrays.asList(Locale.getISOLanguages());
  
  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidIsoLanguage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see jakarta.validation.ConstraintValidator#isValid(java.lang.Object,
   * jakarta.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(String language, ConstraintValidatorContext context) {
    return ISO_LANGUAGES.contains(language);
  }

}
