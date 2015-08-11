package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.enums;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This enumeration includes all accepted data types for a variable.
 * 
 * @author Daniel Katzberg
 *
 */
public enum DataType {

  STRING("string", "string"), NUMERIC("numerisch", "numeric");

  /**
   * This fields holds the accepted German translation.
   */
  private String localeGerman;

  /**
   * This fields holds the accepted English translation.
   */
  private String localeEnglish;

  /**
   * @param localeGerman The accepted value for the German input.
   * @param localEnglish The accepted value for the English input.
   */
  private DataType(String localeGerman, String localEnglish) {
    this.localeGerman = localeGerman;
    this.localeEnglish = localEnglish;
  }

  /**
   * @return Returns the accepted value depending on the actual locale.
   */
  public String getI18nValue() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMANY)) {
      return localeGerman;
      // Default language
    } else {
      return localeEnglish;
    }
  }
}
