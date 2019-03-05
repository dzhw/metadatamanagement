package eu.dzhw.fdz.metadatamanagement.studymanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * The data's availability of a {@link Study} can be in one of these states.
 */
public class DataAvailabilities {
  public static final I18nString AVAILABLE = new ImmutableI18nString("Verfügbar", "Available");
  public static final I18nString IN_PREPARATION =
      new ImmutableI18nString("In Aufbereitung", "In preparation");
  public static final I18nString NOT_AVAILABLE =
      new ImmutableI18nString("Nicht verfügbar", "Not available");
  public static final Set<I18nString> ALL = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList(AVAILABLE, IN_PREPARATION, NOT_AVAILABLE)));
}
