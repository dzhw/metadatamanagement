package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the dataPackage domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataPackageVersionsResource {

  private final DataPackageVersionsService dataPackageVersionsService;

  /**
   * Get the previous 5 versions of the dataPackage.
   * 
   * @param id The id of the dataPackage
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataPackage versions
   */
  @GetMapping("/data-packages/{id}/versions")
  public ResponseEntity<?> findPreviousDataPackageVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<DataPackage> dataPackageVersions =
        dataPackageVersionsService.findPreviousVersions(id, limit, skip);

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(dataPackageVersions);
  }
}
