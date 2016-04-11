package eu.dzhw.fdz.metadatamanagement.common.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.StringUtils;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Ensure that at least one translation (de or en) is given.
 * 
 * @author René Reitmann
 */
public class AtLeastOneLanguageValidator
    implements ConstraintValidator<AtLeastOneLanguage, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(AtLeastOneLanguage constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    return !StringUtils.isEmpty(value.getDe()) || !StringUtils.isEmpty(value.getEn());
  }
}
