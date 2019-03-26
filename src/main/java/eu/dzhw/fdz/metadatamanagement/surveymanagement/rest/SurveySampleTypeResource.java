package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveySampleTypeProvider;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Provides a list of valid sample types that can be set for a Survey.
 */
@Controller
@RequestMapping("/api")
public class SurveySampleTypeResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  private final SurveySampleTypeProvider surveySampleTypeProvider;

  public SurveySampleTypeResource(SurveySampleTypeProvider surveySampleTypeProvider) {
    this.surveySampleTypeProvider = surveySampleTypeProvider;
  }

  /**
   * Returns survey sample types.
   */
  @GetMapping(path = "/surveys/sample-types")
  public ResponseEntity<Set<I18nString>> getSampleTypes() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(surveySampleTypeProvider.getSampleTypes());
  }
}
