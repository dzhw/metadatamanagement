package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.UnitValueProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

/**
 * Default implementation of {@link ValidUnitValue}. {@code null} value will evaluate to
 * {@code true}.
 */
public class ValidUnitValueValidator implements ConstraintValidator<ValidUnitValue, I18nString> {

  private Set<I18nString> unitValues;

  public ValidUnitValueValidator(UnitValueProvider unitValueProvider) {
    this.unitValues = unitValueProvider.getUnitValues();
  }

  @Override
  public boolean isValid(I18nString unit, ConstraintValidatorContext
      constraintValidatorContext) {

    if (unit == null) {
      return true;
    } else {
      return unitValues.contains(unit);
    }
  }
}
