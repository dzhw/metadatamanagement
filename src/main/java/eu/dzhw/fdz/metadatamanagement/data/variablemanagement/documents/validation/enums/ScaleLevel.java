package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.enums;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This enumeration includes all accepted scale levels.
 * 
 * @author Daniel Katzberg
 *
 */
public enum ScaleLevel {
  
  NOMINAL("nominal", "nominal"), 
  ORDINAL("ordinal", "ordinal"), 
  METRIC ("metrisch", "metric");
  
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
  private ScaleLevel(String localeGerman, String localEnglish) {
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
