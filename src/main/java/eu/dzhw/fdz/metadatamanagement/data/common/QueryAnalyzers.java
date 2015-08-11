package eu.dzhw.fdz.metadatamanagement.data.common;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Helper class which returns an elasticsearch query analyzer for the current locale.
 * 
 * @author René Reitmann
 *
 */
public final class QueryAnalyzers {

  private static final HashMap<Locale, String> ANALYZERS = new HashMap<>();

  static {
    ANALYZERS.put(Locale.GERMAN, "german");
    ANALYZERS.put(Locale.ENGLISH, "english");
  }

  /**
   * Get an elasticsearch language analyzer for the current locale.
   * 
   * @return german or english
   */
  public static String getAnalyzerForCurrentLocale() {
    return ANALYZERS.get(LocaleContextHolder.getLocale());
  }

}
