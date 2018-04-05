package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.Release;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DataAcquisitionProjectVersionsService;

/**
 * Rest Controller for retrieving previous version of the data acquisition project domain object.
 * 
 * @author Daniel Katzberg
 */
@RestController
@RequestMapping("/api")
public class DataAcquisitionProjectLastReleaseResource {
  
  @Autowired
  private DataAcquisitionProjectVersionsService projectVersionsService;
  
  /**
   * Get the last released version number of a project.
   * 
   * @param projectId The id of the project
   * 
   * @return A list of previous data acquisition project versions
   */
  @RequestMapping(value = "/data-acquisition-projects/{projectId}/releases/last",
      method = RequestMethod.GET)
  @Timed
  public ResponseEntity<Release> findLastRelease(@PathVariable String projectId) {
    Release lastRelease =
        this.projectVersionsService.findLastRelease(projectId);
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(lastRelease);
  }
}
