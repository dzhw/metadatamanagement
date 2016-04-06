package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The ScaleLevel enumeration.
 */
public class ScaleLevels {
  public static final I18nString ORDINAL = new I18nString("ordinal", "ordinal");
  public static final I18nString NOMINAL = new I18nString("nominal", "nominal");
  public static final I18nString CONTINOUS = new I18nString("kontinuierlich", "continous");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<I18nString>(Arrays.asList(ORDINAL, NOMINAL, CONTINOUS)));
}
