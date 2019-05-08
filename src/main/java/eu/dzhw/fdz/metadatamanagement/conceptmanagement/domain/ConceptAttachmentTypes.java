package eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * All valid types of a {@link ConceptAttachmentMetadata}.
 */
public class ConceptAttachmentTypes {
  public static final I18nString DOCUMENTATION =
      new ImmutableI18nString("Dokumentation", "Documentation");
  public static final I18nString INSTRUMENT =
      new ImmutableI18nString("Instrument", "Instrument");
  public static final I18nString OTHER = new ImmutableI18nString("Sonstiges", "Other");

  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<I18nString>(Arrays.asList(DOCUMENTATION, INSTRUMENT, OTHER)));
}
