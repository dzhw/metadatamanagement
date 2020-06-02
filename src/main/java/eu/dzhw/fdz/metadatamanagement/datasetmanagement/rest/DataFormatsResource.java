package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

/**
 * Provides available {@link DataFormat}.
 */
@Controller
@RequestMapping("/api")
public class DataFormatsResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  /**
   * Return an array of available {@link DataFormat}.
   */
  @GetMapping(path = "/data-sets/data-formats")
  public ResponseEntity<DataFormat[]> getDataFormats() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(DataFormat.values());
  }
}
