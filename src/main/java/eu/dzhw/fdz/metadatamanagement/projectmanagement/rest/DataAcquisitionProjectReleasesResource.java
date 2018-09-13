package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;

/**
 * REST resource for getting previous versions of a {@link DataAcquisitionProject}.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class DataAcquisitionProjectReleasesResource {

  @Autowired
  private DataAcquisitionProjectVersionsService projectVersionsService;

  /**
   * Get the last 100 releases of the data acquisition project.
   * 
   * @param id The id of the project
   * 
   * @return the last 100 releases of the data acquisition project.
   */
  @RequestMapping("/data-acquisition-projects/{id}/releases")
  public ResponseEntity<?> findPreviousDataAcquisitionVersions(@PathVariable String id,
      @RequestParam(name = "noBeta", defaultValue = "true") Boolean noBetaReleases) {
    List<Release> releases = this.projectVersionsService.findAllReleases(id, noBetaReleases);

    if (releases == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(releases);
  }
}
