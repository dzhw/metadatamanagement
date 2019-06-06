package eu.dzhw.fdz.metadatamanagement.common.rest;

import eu.dzhw.fdz.metadatamanagement.common.domain.Country;
import eu.dzhw.fdz.metadatamanagement.common.domain.Language;
import eu.dzhw.fdz.metadatamanagement.common.service.LanguagesProvider;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.CountryCodeProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/api/i18n")
public class LocaleResourceController {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  private final CountryCodeProvider countryCodeProvider;

  private final LanguagesProvider languagesProvider;

  public LocaleResourceController(CountryCodeProvider countryCodeProvider, LanguagesProvider languagesProvider) {
    this.countryCodeProvider = countryCodeProvider;
    this.languagesProvider = languagesProvider;
  }

  /**
   * Returns a set of valid country codes.
   */
  @GetMapping(path = "/country-codes")
  public ResponseEntity<Set<Country>> getCountryCodes() {
    return ResponseEntity.ok()
      .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
      .eTag(ETAG.toString())
      .body(countryCodeProvider.getCountryCodes());
  }

  /**
   * Returns a set of languages.
   * @param locale Used to localize language display names
   * @return Set of languages
   */
  @GetMapping(path = "/languages")
  public ResponseEntity<Set<Language>> getLanguages(@RequestParam(name = "locale", defaultValue = "de") String locale) {
    return ResponseEntity.ok()
      .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
      .eTag(ETAG.toString())
      .body(languagesProvider.getLanguages(locale));
  }
}
