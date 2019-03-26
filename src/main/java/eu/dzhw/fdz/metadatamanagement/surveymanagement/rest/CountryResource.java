package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Country;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.CountryCodeProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Controller for retrieving valid country codes that can be set on a Survey.
 */
@Controller
@RequestMapping("/api")
public class CountryResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  private final CountryCodeProvider countryCodeProvider;

  public CountryResource(CountryCodeProvider countryCodeProvider) {
    this.countryCodeProvider = countryCodeProvider;
  }

  /**
   * Returns a list of valid country codes.
   */
  @GetMapping(path = "/surveys/country-codes")
  public ResponseEntity<Set<Country>> getCountryCodes() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(countryCodeProvider.getCountryCodes());
  }
}
