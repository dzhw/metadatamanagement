package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This enumeration includes all accepted scale levels.
 * 
 * @author Daniel Katzberg
 *
 */
public class ScaleLevel {

  private static final String ENGLISH_METRIC = "metric";
  private static final String ENGLISH_NOMINAL = "nominal";
  private static final String ENGLISH_ORDINAL = "ordinal";

  private static final String GERMAN_METRIC = "metrisch";
  private static final String GERMAN_NOMINAL = "nominal";
  private static final String GERMAN_ORDINAL = "ordinal";
  /**
   * This fields holds the accepted translations of all supported languages.
   */
  private HashMap<Locale, HashSet<String>> scaleLevelMap;

  private static volatile ScaleLevel SCALELEVEL;

  /**
   * @param localeGerman The accepted value for the German input.
   * @param localEnglish The accepted value for the English input.
   */
  private ScaleLevel() {
    this.scaleLevelMap = new HashMap<>();

    // put english
    HashSet<String> allTranslationsEnglish = new HashSet<>();
    allTranslationsEnglish.add(ENGLISH_METRIC);
    allTranslationsEnglish.add(ENGLISH_NOMINAL);
    allTranslationsEnglish.add(ENGLISH_ORDINAL);
    this.scaleLevelMap.put(Locale.ENGLISH, allTranslationsEnglish);

    // put german
    HashSet<String> allTranslationsGerman = new HashSet<>();
    allTranslationsGerman.add(GERMAN_METRIC);
    allTranslationsGerman.add(GERMAN_NOMINAL);
    allTranslationsGerman.add(GERMAN_ORDINAL);
    this.scaleLevelMap.put(Locale.GERMAN, allTranslationsGerman);
  }

  /**
   * @return Return the translated field of metric depending at the actual locale.
   */
  public String getMetricByLocal() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_METRIC;
    } else {
      return ENGLISH_METRIC;
    }

  }

  /**
   * DataTypes is a singleton. This method is the only way to get the data types.
   * 
   * @return The DataTypes singleton object.
   */
  public static ScaleLevel getScaleLevel() {
    if (SCALELEVEL == null) {
      synchronized (ScaleLevel.class) {
        if (SCALELEVEL == null) {
          SCALELEVEL = new ScaleLevel();
        }
      }
    }

    return SCALELEVEL;
  }

  /* GETTER / SETTER */
  public HashMap<Locale, HashSet<String>> getScaleLevelMap() {
    return scaleLevelMap;
  }
}
