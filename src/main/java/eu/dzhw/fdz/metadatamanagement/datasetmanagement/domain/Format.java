package eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * All possible formats of a {@link DataSet}.
 */
public class Format {

  public static final I18nString WIDE = new ImmutableI18nString("breit", "wide");
  public static final I18nString LONG = new ImmutableI18nString("lang", "long");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(WIDE, LONG)));

}
