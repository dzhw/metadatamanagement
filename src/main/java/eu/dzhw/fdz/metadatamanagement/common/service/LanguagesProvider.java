package eu.dzhw.fdz.metadatamanagement.common.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.Language;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Provides a set of known languages.
 */
@Service
public class LanguagesProvider {

  private String[] languages;

  /**
   * Creates a new instance.
   */
  public LanguagesProvider() {
    languages = Locale.getISOLanguages();
  }

  /**
   * Returns a set of known languages.
   *
   * @param locale Locale to use for language names. Falls back to default JVM locale if given locale is unknown.
   * @return Set of languages
   */
  public Set<Language> getLanguages(String locale) {
    Locale clientLanguage = new Locale(locale);
    return Stream.of(languages).map(l -> {
      Locale languageLocale = new Locale(l);
      return new Language(l, languageLocale.getDisplayLanguage(clientLanguage));
    }).collect(Collectors.toCollection(TreeSet::new));
  }
}
