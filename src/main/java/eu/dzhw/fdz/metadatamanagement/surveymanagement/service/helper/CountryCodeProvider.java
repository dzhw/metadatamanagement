package eu.dzhw.fdz.metadatamanagement.surveymanagement.service.helper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import eu.dzhw.fdz.metadatamanagement.common.domain.Country;

/**
 * Provides a list of valid country codes.
 */
public class CountryCodeProvider {

  public static final Set<Country> COUNTRY_CODES;

  static {
    Set<Country> countryCodes = new HashSet<>();
    for (String countryCode : Locale.getISOCountries()) {
      Locale locale = new Locale("", countryCode);
      String deDisplayName = locale.getDisplayCountry(Locale.GERMAN);
      String enDisplayName = locale.getDisplayCountry(Locale.ENGLISH);
      countryCodes.add(new Country(countryCode, deDisplayName, enDisplayName));
    }
    COUNTRY_CODES = Collections.unmodifiableSet(countryCodes);
  }
}
