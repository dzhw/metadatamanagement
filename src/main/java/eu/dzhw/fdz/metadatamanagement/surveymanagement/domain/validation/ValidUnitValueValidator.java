package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link ValidUnitValue}. {@code null} value will evaluate to
 * {@code true}.
 */
public class ValidUnitValueValidator implements ConstraintValidator<ValidUnitValue, I18nString> {

  private static final List<I18nString> VALID_UNIT_TYPES;

  static {
    VALID_UNIT_TYPES = new ArrayList<>();
    VALID_UNIT_TYPES.add(new I18nString("Auszubildende", "Trainees"));
    VALID_UNIT_TYPES.add(new I18nString("Behörden", "Authorities"));
    VALID_UNIT_TYPES.add(new I18nString("Berufsanfänger", "Entrants"));
    VALID_UNIT_TYPES.add(new I18nString("Eltern", "Parents"));
    VALID_UNIT_TYPES.add(new I18nString("Erwachsene", "Adults"));
    VALID_UNIT_TYPES.add(new I18nString("Hochschulabsolventen", "College Graduates"));
    VALID_UNIT_TYPES.add(new I18nString("Jugendliche", "Adolescents"));
    VALID_UNIT_TYPES.add(new I18nString("Kindergartenkinder", "Kindergardeners"));
    VALID_UNIT_TYPES.add(new I18nString("Lehrkräfte", "Teachers"));
    VALID_UNIT_TYPES.add(new I18nString("Promovierte", "Postdoc")); // Not in official KV
    VALID_UNIT_TYPES.add(new I18nString("Promovierende", "Doctoral Candidates"));
    VALID_UNIT_TYPES.add(new I18nString("Pädagogisches Personal", "Educational Staff"));
    VALID_UNIT_TYPES.add(new I18nString("Referendare", "Student Teachers"));
    VALID_UNIT_TYPES.add(new I18nString("Schulabgänger", "School Leaver"));
    VALID_UNIT_TYPES.add(new I18nString("Schulen", "Schools"));
    VALID_UNIT_TYPES.add(new I18nString("Schulleitung", "School Management"));
    VALID_UNIT_TYPES.add(new I18nString("Schüler", "Pupils"));
    VALID_UNIT_TYPES.add(new I18nString("Sonstiges", "Other"));
    VALID_UNIT_TYPES.add(new I18nString("Studienzugangsberechtigte",
        "Authorized Student"));
    VALID_UNIT_TYPES.add(new I18nString("Studierende", "Students"));
    VALID_UNIT_TYPES.add(new I18nString("Vorschulkinder", "Preschoolers"));
    VALID_UNIT_TYPES.add(new I18nString("Wissenschaftliches Personal",
        "Scientific Staff"));
  }

  @Override
  public boolean isValid(I18nString unit, ConstraintValidatorContext
      constraintValidatorContext) {

    if (unit == null) {
      return true;
    } else {
      return VALID_UNIT_TYPES.contains(unit);
    }
  }
}
