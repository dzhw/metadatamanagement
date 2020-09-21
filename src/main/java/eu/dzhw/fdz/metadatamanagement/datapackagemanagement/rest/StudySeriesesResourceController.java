package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.repository.DataPackageRepository;
import lombok.RequiredArgsConstructor;

/**
 * Study Series REST Controller which retrieves all study serieses present in 
 * the primary data store.
 * 
 * @author René Reitmann
 */
@RestController
@RequiredArgsConstructor
public class StudySeriesesResourceController {

  private final DataPackageRepository dataPackageRepository;
 
  /**
   * Get all available dataPackage serieses.
   */
  @RequestMapping(value = "/api/study-serieses", method = RequestMethod.GET)
  public ResponseEntity<List<I18nString>> findAllStudySerieses() {
    List<I18nString> studySerieses = dataPackageRepository.findAllStudySerieses();
    return ResponseEntity.ok().cacheControl(CacheControl.noStore())
        .body(studySerieses);
  }
}
