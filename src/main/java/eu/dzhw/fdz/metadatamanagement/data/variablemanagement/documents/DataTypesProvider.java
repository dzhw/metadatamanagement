package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * This class includes all accepted data types for a variable.
 * 
 * @author Daniel Katzberg
 *
 */
@Component
public class DataTypesProvider {

  public static final String ENGLISH_STRING = "string";
  public static final String ENGLISH_NUMERIC = "numeric";

  public static final String GERMAN_STRING = "string";
  public static final String GERMAN_NUMERIC = "numerisch";

  /**
   * This fields holds the accepted translations of all supported languages.
   */
  private HashMap<Locale, HashSet<String>> dataTypesMap;

  /**
   * The basic constructor fills a map with valid language translations of the String and Numeric
   * data type.
   */
  public DataTypesProvider() {
    this.dataTypesMap = new HashMap<>();

    // put english
    HashSet<String> allTranslationsEnglish = new HashSet<>();
    allTranslationsEnglish.add(ENGLISH_STRING);
    allTranslationsEnglish.add(ENGLISH_NUMERIC);
    this.dataTypesMap.put(Locale.ENGLISH, allTranslationsEnglish);

    // put german
    HashSet<String> allTranslationsGerman = new HashSet<>();
    allTranslationsGerman.add(GERMAN_STRING);
    allTranslationsGerman.add(GERMAN_NUMERIC);
    this.dataTypesMap.put(Locale.GERMAN, allTranslationsGerman);
  }
  
  /**
   * @return Returns all data types depending by the locale. 
   */
  public HashSet<String> getAllDataTypes() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return this.dataTypesMap.get(Locale.GERMAN);
    } else {
      return this.dataTypesMap.get(Locale.ENGLISH);
    }
  }

  /**
   * @return The language depended versions of numeric.
   */
  public String getNumericValueByLocale() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_NUMERIC;
    } else {
      return ENGLISH_NUMERIC;
    }
  }

  /**
   * @return The language depended versions of numeric.
   */
  public String getStringValueByLocale() {
    if (LocaleContextHolder.getLocale().equals(Locale.GERMAN)) {
      return GERMAN_STRING;
    } else {
      return ENGLISH_STRING;
    }
  }

  /* GETTER / SETTER */
  public HashMap<Locale, HashSet<String>> getDataTypesMap() {
    return dataTypesMap;
  }

}
