package eu.dzhw.fdz.metadatamanagement.questionmanagement.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;

/**
 * Question Types.
 */
public class QuestionTypes {
  public static final I18nString OPEN = new I18nString("Offen", "Open");
  public static final I18nString SINGLE_CHOICE = new I18nString("Single Choice", "Single Choice");
  public static final I18nString MULTIPLE_CHOICE = 
      new I18nString("Mehrfachnennung", "Multiple Choice");
  public static final I18nString ITEM_SET = new I18nString("Itembatterie", "Item Set");
  public static final I18nString GRID = new I18nString("Matrix", "Grid");
  public static final Set<I18nString> ALL =
      Collections.unmodifiableSet(new HashSet<>(Arrays.asList(OPEN, SINGLE_CHOICE, 
          MULTIPLE_CHOICE, ITEM_SET, GRID)));
}