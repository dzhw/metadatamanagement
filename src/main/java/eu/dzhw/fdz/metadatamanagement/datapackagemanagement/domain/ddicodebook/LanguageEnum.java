package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

/**
 * Enum representing available language abbreviations.
 */
public enum LanguageEnum {
  de("de"), en("en");

  /**
   * The language string.
   */
  public final String languageString;

  /**
   * Construct the enum.
   *
   * @param languageString The language string.
   */
  LanguageEnum(String languageString) {
    this.languageString = languageString;
  }
}
