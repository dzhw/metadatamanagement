package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormats;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Provides available {@link DataFormats}.
 */
@Controller
@RequestMapping("/api")
public class DataFormatsResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  /**
   * Return an array of available {@link DataFormats}.
   */
  @GetMapping(path = "/data-sets/data-formats")
  public ResponseEntity<DataFormats[]> getDataFormats() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(DataFormats.values());
  }
}
