package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySampleTypeProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Default implementation of {@link ValidSampleType}. {@code null} values are
 * validated to {@code true}.
 */
public class ValidSampleTypeValidator implements ConstraintValidator<ValidSampleType, I18nString> {

  private Set<I18nString> sampleTypes;

  public ValidSampleTypeValidator(SurveySampleTypeProvider surveySampleTypeProvider) {
    sampleTypes = surveySampleTypeProvider.getSampleTypes();
  }

  @Override
  public boolean isValid(I18nString i18nString, ConstraintValidatorContext context) {
    if (i18nString == null) {
      return true;
    }

    return sampleTypes.contains(i18nString);
  }
}
