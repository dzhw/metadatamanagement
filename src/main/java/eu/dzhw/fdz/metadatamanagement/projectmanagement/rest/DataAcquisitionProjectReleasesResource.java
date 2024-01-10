package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * REST resource for getting previous versions of a {@link DataAcquisitionProject}.
 *
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataAcquisitionProjectReleasesResource {

  private final DataAcquisitionProjectVersionsService projectVersionsService;

  /**
   * Get the last 100 releases of the data acquisition project.
   *
   * @param id The id of the project
   *
   * @return the last 100 releases of the data acquisition project.
   */
  @GetMapping("/data-acquisition-projects/{id}/releases")
  public ResponseEntity<?> findPreviousDataAcquisitionVersions(@PathVariable String id,
      @RequestParam(name = "excludePreReleased", defaultValue = "false") Boolean excludePreReleased,
      @RequestParam(name = "noBeta", defaultValue = "true") Boolean noBetaReleases,
      @RequestParam(name = "onlyNotHidden", defaultValue = "false") Boolean onlyNotHiddenVersions) {
    List<Release> releases = this.projectVersionsService.findAllReleases(id, excludePreReleased, noBetaReleases, onlyNotHiddenVersions);

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(releases);
  }
}
