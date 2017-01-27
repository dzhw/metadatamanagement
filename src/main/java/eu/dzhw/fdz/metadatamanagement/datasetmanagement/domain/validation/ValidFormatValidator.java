package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.google.common.base.Strings;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.Format;

/**
 * Validator of the format of datasets. Only values from the class {@link Format} are allowed.
 * 
 * @author Daniel Katzberg
 *
 */
public class ValidFormatValidator implements ConstraintValidator<ValidFormat, I18nString> {

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
   */
  @Override
  public void initialize(ValidFormat constraintAnnotation) {}

  /*
   * (non-Javadoc)
   * 
   * @see javax.validation.ConstraintValidator#isValid(java.lang.Object,
   * javax.validation.ConstraintValidatorContext)
   */
  @Override
  public boolean isValid(I18nString format, ConstraintValidatorContext context) {

    if (format == null) {
      return true;
    }

    if (Strings.isNullOrEmpty(format.getDe()) && Strings.isNullOrEmpty(format.getEn())) {
      return true;
    }

    // check for scale levels
    return Format.ALL.contains(format);
  }
}
