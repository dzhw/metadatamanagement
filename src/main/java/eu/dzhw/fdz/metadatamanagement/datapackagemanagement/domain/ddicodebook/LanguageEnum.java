package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.ddicodebook;

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
