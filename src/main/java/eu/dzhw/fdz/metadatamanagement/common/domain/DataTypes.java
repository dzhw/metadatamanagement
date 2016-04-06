package eu.dzhw.fdz.metadatamanagement.common.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The DataType enumeration.
 */
public class DataTypes {

  public static final I18nString STRING = new I18nString("string", "string");
  public static final I18nString NUMERIC = new I18nString("numerisch", "numeric");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<I18nString>(Arrays.asList(STRING, NUMERIC)));
}
