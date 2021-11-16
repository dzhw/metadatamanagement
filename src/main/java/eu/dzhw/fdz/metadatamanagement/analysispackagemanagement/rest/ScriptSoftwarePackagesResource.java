package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.domain.SoftwarePackages;

/**
 * Provides a list of all available {@link SoftwarePackages}.
 */
@Controller
@RequestMapping("/api")
public class ScriptSoftwarePackagesResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  /**
   * Returns survey sample types.
   */
  @GetMapping(path = "/analysis-packages/scripts/software-packages")
  public ResponseEntity<List<String>> getSoftwarePackages() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(SoftwarePackages.ALL);
  }
}
