package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.validation;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * Default implementation of {@link ValidSampleType}. {@code null} values are
 * validated to {@code true}.
 */
public class ValidSampleTypeValidator implements ConstraintValidator<ValidSampleType, I18nString> {

  private static final List<I18nString> CONTROLLED_SAMPLE_VOCABULARY;

  static {
    CONTROLLED_SAMPLE_VOCABULARY = Arrays.asList(
        new I18nString("Kombination aus Wahrscheinlichkeits- und Nicht-"
            + "Wahrscheinlichkeitsauswahl", "Mixed probability and non-probability Sample"),
        new I18nString("Nicht-Wahrscheinlichkeitsauswahl", "Non-probability Sample"),
        new I18nString("Nicht-Wahrscheinlichkeitsauswahl: Willkürliche Auswahl",
            "Non-probability Sample: Availability Sample"),
        new I18nString("Nicht-Wahrscheinlichkeitsauswahl: Bewusste Auswahl",
            "Non-probability Sample: Purposive Sample"),
        new I18nString("Nicht-Wahrscheinlichkeitsauswahl: Quotenstichprobe",
            "Non-probability Sample: Quota Sample"),
        new I18nString("Nicht-Wahrscheinlichkeitsauswahl: Respondenten-gesteuerte Auswahl",
            "Non-probability Sample: Respondent-assisted Sample"),
        new I18nString("Sonstige", "Other"),
        new I18nString("Wahrscheinlichkeitsauswahl", "Probability Sample"),
        new I18nString("Wahrscheinlichkeitsauswahl: Klumpenauswahl",
            "Probability Sample: Cluster Sample"),
        new I18nString("Wahrscheinlichkeitsauswahl: Mehrstufige Zufallsauswahl",
            "Probability Sample: Multistage Sample"),
        /*
         * Some of the following strings are misspelled on purpose, because that's what VFDB
         * expects.
         */
        new I18nString("Wahrscheinlichkeitsauswahl: Einfache KlumpenauswahI",
            "Probability Sample: Simple Cluster Sampie"),
        new I18nString("Wahrscheinlichkeitsauswahl: Einfache Zufallsauswahl",
            "Probability Sample: Simple Random Sampie"),
        new I18nString("Wahrscheinlichkeitsauswahl: Geschichtete Klumpenauswahl",
            "Probability Sample: Stratified Cluster Sampie"),
        new I18nString("Wahrscheinlichkeitsauswahl: Geschichtete Zufallsauswahl",
            "Probability Sample: Stratified Sampie"),
        new I18nString("Wahrscheinlichkeitsauswahl: Disproportional geschichtete "
            + "Zufallsauswahl", "Probability Sample: Stratified Sample: Disproportional"),
        new I18nString("Wahrscheinlichkeitsauswahl: Proportional geschichtete Zufallsauswahl",
            "Probability Sample: Stratified Sample: Proportional"),
        new I18nString("Wahrscheinlichkeitsauswahl: Systematische Zufallsauswahl",
            "Probability Sample: Systematic Random Sample"),
        new I18nString("TotaI universe/Complete Enumeration", "Vollerhebung")

    );
  }

  @Override
  public boolean isValid(I18nString i18nString, ConstraintValidatorContext context) {
    if (i18nString == null) {
      return true;
    }

    return CONTROLLED_SAMPLE_VOCABULARY.contains(i18nString);
  }
}
