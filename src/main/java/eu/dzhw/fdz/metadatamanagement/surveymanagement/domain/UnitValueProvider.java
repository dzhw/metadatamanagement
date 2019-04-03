package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides valid unit values for Survey.
 */
@Service
public class UnitValueProvider {

  private static final Set<I18nString> UNIT_VALUES;

  static {
    UNIT_VALUES = new HashSet<>();
    UNIT_VALUES.add(new ImmutableI18nString("Auszubildende", "Trainees"));
    UNIT_VALUES.add(new ImmutableI18nString("Behörden", "Authorities"));
    UNIT_VALUES.add(new ImmutableI18nString("Berufsanfänger", "Entrants"));
    UNIT_VALUES.add(new ImmutableI18nString("Eltern", "Parents"));
    UNIT_VALUES.add(new ImmutableI18nString("Erwachsene", "Adults"));
    UNIT_VALUES.add(new ImmutableI18nString("Hochschulabsolventen", "College Graduates"));
    UNIT_VALUES.add(new ImmutableI18nString("Jugendliche", "Adolescents"));
    UNIT_VALUES.add(new ImmutableI18nString("Kindergartenkinder", "Kindergardeners"));
    UNIT_VALUES.add(new ImmutableI18nString("Lehrkräfte", "Teachers"));
    UNIT_VALUES.add(new ImmutableI18nString("Promovierte", "Postdoc")); // Not in official KV
    UNIT_VALUES.add(new ImmutableI18nString("Promovierende", "Doctoral Candidates"));
    UNIT_VALUES.add(new ImmutableI18nString("Pädagogisches Personal", "Educational Staff"));
    UNIT_VALUES.add(new ImmutableI18nString("Referendare", "Student Teachers"));
    UNIT_VALUES.add(new ImmutableI18nString("Schulabgänger", "School Leaver"));
    UNIT_VALUES.add(new ImmutableI18nString("Schulen", "Schools"));
    UNIT_VALUES.add(new ImmutableI18nString("Schulleitung", "School Management"));
    UNIT_VALUES.add(new ImmutableI18nString("Schüler", "Pupils"));
    UNIT_VALUES.add(new ImmutableI18nString("Sonstiges", "Other"));
    UNIT_VALUES.add(new ImmutableI18nString("Studienzugangsberechtigte",
        "Authorized Student"));
    UNIT_VALUES.add(new ImmutableI18nString("Studierende", "Students"));
    UNIT_VALUES.add(new ImmutableI18nString("Vorschulkinder", "Preschoolers"));
    UNIT_VALUES.add(new ImmutableI18nString("Wissenschaftliches Personal",
        "Scientific Staff"));
  }

  /**
   * Returns valid unit values.
   */
  public Set<I18nString> getUnitValues() {
    return Collections.unmodifiableSet(UNIT_VALUES);
  }
}
