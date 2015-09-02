package eu.dzhw.fdz.metadatamanagement.web.common.exceptions.utils;

import java.util.Locale;

import eu.dzhw.fdz.metadatamanagement.data.variablemanagement.documents.VariableDocument;

/**
 * This class allows exceptions for different language support. It checks the {@code Locale} in
 * different ways. The result of the methods are variable names from the message properties for an
 * explicit use of the correct attribute. The attribute means means a entry in the message
 * properties. Every entry in a message properties is on the left side a attribute and on the right
 * side a value.
 * 
 * @author Daniel Katzberg
 *
 */
public class ExceptionLanguageUtils {

  private static final String NAV_LANGUAGE_GERMAN = "nav.language.german";
  private static final String NAV_LANGUAGE_ENGLISH = "nav.language.english";

  /**
   * With readable value should be represent for a used language? This method checks a given
   * {@code Locale} and returns the depending attribute from the message properties.
   * 
   * @param locale A given Locale.
   * @return A attribute name from the message property which returns a human readable entry of a
   *         language
   */
  public String getCorrectReadableLanguage(Locale locale) {
    // English
    if (locale == Locale.ENGLISH) {
      return NAV_LANGUAGE_ENGLISH;
      // Germen
    } else {
      return NAV_LANGUAGE_GERMAN;
    }
  }

  /**
   * @param clazz The class of object which throws the exception.
   * @return A attribute name from the message property which returns a human readable entry of a
   *         document type
   */
  public String getCorrectReadableLanguage(@SuppressWarnings("rawtypes") Class clazz) {
    // English
    if (VariableDocument.class == clazz) {
      return "documenttype.variable";
    }

    return "";
  }
}
