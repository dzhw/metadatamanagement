package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sample types for survey. This list is based on official VFDB vocabulary.
 * <a href="https://mdr.iqb.hu-berlin.de/#/catalog/1d791cc7-6d8d-dd35-b1ef-0eec9c31bbb5">
 * Catalog: GNERD: Sampling Procedure Educational Research (Version 1.0)
 * </a>
 */
@Service
public class SurveySampleTypeProvider {

  private static final Set<I18nString> CONTROLLED_SAMPLE_VOCABULARY;

  static {
    CONTROLLED_SAMPLE_VOCABULARY = new HashSet<>();
    CONTROLLED_SAMPLE_VOCABULARY.addAll(Arrays.asList(
        new ImmutableI18nString("Kombination aus Wahrscheinlichkeits- und Nicht-"
            + "Wahrscheinlichkeitsauswahl", "Mixed probability and non-probability Sample"),
        new ImmutableI18nString("Nicht-Wahrscheinlichkeitsauswahl", "Non-probability "
            + "Sample"),
        new ImmutableI18nString("Nicht-Wahrscheinlichkeitsauswahl: Willkürliche Auswahl",
            "Non-probability Sample: Availability Sample"),
        new ImmutableI18nString("Nicht-Wahrscheinlichkeitsauswahl: Bewusste Auswahl",
            "Non-probability Sample: Purposive Sample"),
        new ImmutableI18nString("Nicht-Wahrscheinlichkeitsauswahl: Quotenstichprobe",
            "Non-probability Sample: Quota Sample"),
        new ImmutableI18nString("Nicht-Wahrscheinlichkeitsauswahl: Respondenten-gesteuerte "
            + "Auswahl", "Non-probability Sample: Respondent-assisted Sample"),
        new ImmutableI18nString("Sonstige", "Other"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl", "Probability Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Klumpenauswahl",
            "Probability Sample: Cluster Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Mehrstufige Zufallsauswahl",
            "Probability Sample: Multistage Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Einfache KlumpenauswahI",
            "Probability Sample: Simple Cluster Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Einfache Zufallsauswahl",
            "Probability Sample: Simple Random Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Geschichtete Klumpenauswahl",
            "Probability Sample: Stratified Cluster Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Geschichtete Zufallsauswahl",
            "Probability Sample: Stratified Sample"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Disproportional geschichtete "
            + "Zufallsauswahl", "Probability Sample: Stratified Sample: Disproportional"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Proportional geschichtete "
            + "Zufallsauswahl",
            "Probability Sample: Stratified Sample: Proportional"),
        new ImmutableI18nString("Wahrscheinlichkeitsauswahl: Systematische Zufallsauswahl",
            "Probability Sample: Systematic Random Sample"),
        new ImmutableI18nString("Vollerhebung", "Total universe/Complete Enumeration")));
  }


  /**
   * Returns the set of valid survey sample types. The set and it's contents are immutable.
   */
  public Set<I18nString> getSampleTypes() {
    return Collections.unmodifiableSet(CONTROLLED_SAMPLE_VOCABULARY);
  }
}
