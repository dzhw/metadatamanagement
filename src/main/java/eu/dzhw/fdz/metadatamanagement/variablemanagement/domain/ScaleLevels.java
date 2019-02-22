package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * The scale level (or level of measurement) classifies the nature of information within the values
 * assigned to a {@link Variable} ({@link ValidResponse}s). It determines which mathematical
 * operations can be performed with the values.
 * 
 */
public class ScaleLevels {
  public static final I18nString ORDINAL = new ImmutableI18nString("ordinal", "ordinal");
  public static final I18nString NOMINAL = new ImmutableI18nString("nominal", "nominal");
  public static final I18nString RATIO = new ImmutableI18nString("verhältnis", "ratio");
  public static final I18nString INTERVAL = new ImmutableI18nString("intervall", "interval");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<I18nString>(Arrays.asList(ORDINAL, NOMINAL, RATIO, INTERVAL)));
}
