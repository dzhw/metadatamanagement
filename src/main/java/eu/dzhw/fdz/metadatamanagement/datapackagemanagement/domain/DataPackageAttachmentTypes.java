package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * All valid types of a {@link DataPackageAttachmentMetadata}.
 */
public class DataPackageAttachmentTypes {
  public static final I18nString METHOD_REPORT =
      new ImmutableI18nString("Daten- und Methodenbericht", "Method Report");
  public static final I18nString RELEASE_NOTES =
      new ImmutableI18nString("Release Notes", "Release Notes");
  public static final I18nString OTHER = new ImmutableI18nString("Sonstiges", "Other");

  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<I18nString>(Arrays.asList(METHOD_REPORT, RELEASE_NOTES, OTHER)));
}
