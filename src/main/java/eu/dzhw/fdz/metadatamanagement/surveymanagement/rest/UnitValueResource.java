package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.UnitValueProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Resource for accessing valid unit values.
 */
@Controller
@RequestMapping("/api")
public class UnitValueResource {

  private Set<I18nString> unitValues;
  private Long etag;

  public UnitValueResource(UnitValueProvider unitValueProvider) {
    this.unitValues = unitValueProvider.getUnitValues();
    etag = ManagementFactory.getRuntimeMXBean().getStartTime();
  }

  /**
   * Returns valid unit values for Survey.
   */
  @RequestMapping(path = "/surveys/unit-values")
  public ResponseEntity<Set<I18nString>> getUnitValues() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(etag.toString())
        .body(unitValues);
  }
}
