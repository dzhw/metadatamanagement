package eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.validation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.DataTypes;

/**
 * Default implementation of {@link ValidSurveyDataTypes}.
 */
class ValidSurveyDataTypesValidator
    implements ConstraintValidator<ValidSurveyDataTypes, List<I18nString>> {

  @Override
  public boolean isValid(List<I18nString> dataTypes,
      ConstraintValidatorContext constraintValidatorContext) {

    if (dataTypes == null) {
      return true;
    }

    return dataTypes.stream().allMatch(DataTypes.ALL::contains);
  }
}
