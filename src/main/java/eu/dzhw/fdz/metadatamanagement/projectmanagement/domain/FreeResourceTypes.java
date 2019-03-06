package eu.dzhw.fdz.metadatamanagement.projectmanagement.domain;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * Resource Types as they are harvested from DARA by the VFDB.
 */
public class FreeResourceTypes {
  public static final I18nString SURVEY_DATA =
      new ImmutableI18nString("Umfrage- und Aggregatdaten", "Survey and Aggregate Data");
  public static final I18nString COMPETENCE_DATA =
      new ImmutableI18nString("Kompetenz- und Leistungsdaten", "Competence and Performance Data");
  public static final I18nString MIXED_DATA =
      new ImmutableI18nString("Umfrage- und Aggregatdaten; Kompetenz- und Leistungsdaten",
          "Survey and Aggregate Data; Competence and Performance Data");
}
