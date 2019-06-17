package eu.dzhw.fdz.metadatamanagement.surveymanagement.service;

import eu.dzhw.fdz.metadatamanagement.common.domain.Country;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Provides a list of valid country codes.
 */
@Service
public class CountryCodeProvider {

  private static final Set<Country> COUNTRY_CODES;

  static {
    COUNTRY_CODES = new HashSet<>();
    for (String countryCode : Locale.getISOCountries()) {
      Locale locale = new Locale("", countryCode);
      String deDisplayName = locale.getDisplayCountry(Locale.GERMAN);
      String enDisplayName = locale.getDisplayCountry(Locale.ENGLISH);
      COUNTRY_CODES.add(new Country(countryCode, deDisplayName, enDisplayName));
    }
  }

  /**
   * Returns a set of valid country codes.
   */
  public Set<Country> getCountryCodes() {
    return Collections.unmodifiableSet(COUNTRY_CODES);
  }
}
