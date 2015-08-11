package eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.validation.types;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

/**
 * This class includes all accepted data types for a variable.
 * 
 * @author Daniel Katzberg
 *
 */
public class DataTypes {

  private static final String ENGLISH_STRING = "string";
  private static final String ENGLISH_NUMERIC = "numeric";

  private static final String GERMAN_STRING = "string";
  private static final String GERMAN_NUMERIC = "numerisch";

  private static volatile DataTypes DATATYPES;

  /**
   * This fields holds the accepted translations of all supported languages.
   */
  private HashMap<Locale, HashSet<String>> dataTypesMap;

  private DataTypes() {
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
   * DataTypes is a singleton. This method is the only way to get the data types.
   * 
   * @return The DataTypes singleton object.
   */
  public static DataTypes getDataTypes() {
    if (DATATYPES == null) {
      synchronized (DataTypes.class) {
        if (DATATYPES == null) {
          DATATYPES = new DataTypes();
        }
      }
    }

    return DATATYPES;
  }

  /* GETTER / SETTER */
  public HashMap<Locale, HashSet<String>> getDataTypesMap() {
    return dataTypesMap;
  }

}
