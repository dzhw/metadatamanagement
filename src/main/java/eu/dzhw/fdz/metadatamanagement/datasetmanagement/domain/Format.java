package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Format for Data Sets Enumeration.
 * @author Daniel Katzberg
 *
 */
public class Format {

  public static final I18nString WIDE = new I18nString("breit", "wide");
  public static final I18nString LONG = new I18nString("lang", "long");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList(WIDE, LONG)));
  
}
