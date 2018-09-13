package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;

/**
 * REST Controller which retrieves all access way present in the primary data store for a given
 * study.
 * 
 * @author René Reitmann
 */
@RestController
public class StudyAccessWaysResourceController {

  private DataSetRepository dataSetRepository;
  
  @Autowired
  public StudyAccessWaysResourceController(DataSetRepository dataSetRepository) {
    this.dataSetRepository = dataSetRepository;
  }

  /**
   * Get all available study serieses.
   */
  @RequestMapping(value = "/api/studies/{studyId}/access-ways", method = RequestMethod.GET)
  public ResponseEntity<List<String>> findAllAccessWays(@PathVariable String studyId) {
    List<String> accessWays = dataSetRepository.findAllAccessWays(studyId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore())
        .body(accessWays);
  }
}
