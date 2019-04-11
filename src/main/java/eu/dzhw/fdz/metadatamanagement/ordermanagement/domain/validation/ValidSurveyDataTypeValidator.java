package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.SurveyDataTypes;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Default implementation of {@link ValidSurveyDataType}.
 */
class ValidSurveyDataTypeValidator implements ConstraintValidator<ValidSurveyDataType, I18nString> {

  @Override
  public boolean isValid(I18nString i18nString,
                         ConstraintValidatorContext constraintValidatorContext) {
    return SurveyDataTypes.ALL.contains(i18nString);
  }
}
