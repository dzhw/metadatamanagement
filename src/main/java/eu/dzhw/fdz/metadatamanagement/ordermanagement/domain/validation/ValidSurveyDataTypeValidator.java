package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDataTypes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default implementation of {@link ValidSurveyDataType}. {@code null} values are evaluated to
 * {@code true}.
 */
class ValidSurveyDataTypeValidator implements ConstraintValidator<ValidSurveyDataType, I18nString> {

  @Override
  public boolean isValid(I18nString i18nString,
                         ConstraintValidatorContext constraintValidatorContext) {

    if (i18nString == null) {
      return true;
    }

    return SurveyDataTypes.ALL.contains(i18nString);
  }
}
