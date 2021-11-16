package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataFormat;

/**
 * Provides available {@link DataFormat}.
 */
@Controller
public class DataFormatsResource {

  private static final Long ETAG = ManagementFactory.getRuntimeMXBean().getStartTime();

  /**
   * Return an array of available {@link DataFormat}.
   */
  @GetMapping(path = "/api/data-sets/data-formats")
  public ResponseEntity<DataFormat[]> getDataFormats() {
    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(0, TimeUnit.DAYS).mustRevalidate().cachePublic())
        .eTag(ETAG.toString())
        .body(DataFormat.values());
  }
}
