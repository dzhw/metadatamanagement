package eu.dzhw.fdz.metadatamanagement.variablemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * The ScaleLevel enumeration.
 */
public class ScaleLevels {
  public static final I18nString ORDINAL = new I18nString("ordinal", "ordinal");
  public static final I18nString NOMINAL = new I18nString("nominal", "nominal");
  public static final I18nString RATIO = new I18nString("verhältnis", "ratio");
  public static final I18nString INTERVAL = new I18nString("intervall", "interval");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<I18nString>(Arrays.asList(ORDINAL, NOMINAL, RATIO, INTERVAL)));
}
