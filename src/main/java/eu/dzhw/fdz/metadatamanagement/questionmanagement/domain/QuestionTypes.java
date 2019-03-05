package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.domain.ImmutableI18nString;

/**
 * All valid types of a {@link Question}.
 */
public class QuestionTypes {
  public static final I18nString OPEN = new ImmutableI18nString("Offen", "Open");
  public static final I18nString SINGLE_CHOICE =
      new ImmutableI18nString("Einfachnennung", "Single Choice");
  public static final I18nString MULTIPLE_CHOICE =
      new ImmutableI18nString("Mehrfachnennung", "Multiple Choice");
  public static final I18nString ITEM_SET = new ImmutableI18nString("Itembatterie", "Item Set");
  public static final I18nString GRID = new ImmutableI18nString("Matrix", "Grid");
  public static final I18nString UNDOCUMENTED =
      new ImmutableI18nString("Nicht erfasst", "Undocumented");

  public static final Set<I18nString> ALL = Collections.unmodifiableSet(new HashSet<>(
      Arrays.asList(OPEN, SINGLE_CHOICE, MULTIPLE_CHOICE, ITEM_SET, GRID, UNDOCUMENTED)));
}
