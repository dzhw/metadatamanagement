package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.cglib.beans.ImmutableBean;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * All valid types of a {@link Question}.
 */
public class QuestionTypes {
  public static final I18nString OPEN =
      (I18nString) ImmutableBean.create(new I18nString("Offen", "Open"));
  public static final I18nString SINGLE_CHOICE =
      (I18nString) ImmutableBean.create(new I18nString("Einfachnennung", "Single Choice"));
  public static final I18nString MULTIPLE_CHOICE =
      (I18nString) ImmutableBean.create(new I18nString("Mehrfachnennung", "Multiple Choice"));
  public static final I18nString ITEM_SET =
      (I18nString) ImmutableBean.create(new I18nString("Itembatterie", "Item Set"));
  public static final I18nString GRID =
      (I18nString) ImmutableBean.create(new I18nString("Matrix", "Grid"));
  public static final I18nString UNDOCUMENTED =
      (I18nString) ImmutableBean.create(new I18nString("Nicht erfasst", "Undocumented"));

  public static final Set<I18nString> ALL = Collections.unmodifiableSet(new HashSet<>(
      Arrays.asList(OPEN, SINGLE_CHOICE, MULTIPLE_CHOICE, ITEM_SET, GRID, UNDOCUMENTED)));
}
