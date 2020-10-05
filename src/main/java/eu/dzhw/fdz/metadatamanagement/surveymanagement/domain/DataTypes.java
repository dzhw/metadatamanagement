package eu.dzhw.fdz.metadatamanagement.surveymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * Types of data, which a {@link Survey} can produce.
 */
public class DataTypes {

  // Valid DataPackage Data Types
  public static final I18nString QUANTITATIVE_DATA =
      new ImmutableI18nString("Quantitative Daten", "Quantitative Data");
  public static final I18nString QUALITATIVE_DATA =
      new ImmutableI18nString("Qualitative Daten", "Qualitative Data");

  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList(QUANTITATIVE_DATA, QUALITATIVE_DATA)));
}
