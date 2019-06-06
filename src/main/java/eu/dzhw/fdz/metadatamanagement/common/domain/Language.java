package eu.dzhw.fdz.metadatamanagement.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wrapper for a language code and it's respective display name.
 */
@Data
@AllArgsConstructor
public class Language implements Comparable<Language>{

  /**
   * Language code.
   */
  private final String languageCode;

  /**
   * Display name.
   */
  private final String displayName;

  @Override
  public int compareTo(Language language) {
    return displayName.compareTo(language.displayName);
  }
}
