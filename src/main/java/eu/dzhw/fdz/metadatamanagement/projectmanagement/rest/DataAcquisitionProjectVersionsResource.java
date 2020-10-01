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
public class DataAcquisitionProjectVersionsResource {

  private final DataAcquisitionProjectVersionsService projectVersionsService;

  /**
   * Get the previous 5 versions of the data acquisition project.
   * 
   * @param id The id of the project
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous data acquisition project versions
   */
  @GetMapping("/data-acquisition-projects/{id}/versions")
  public ResponseEntity<?> findPreviousDataAcquisitionVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<DataAcquisitionProject> projectsVersions =
        this.projectVersionsService.findPreviousVersions(id, limit, skip);

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(projectsVersions);
  }
}
