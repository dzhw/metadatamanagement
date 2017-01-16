package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyService;

/**
 * REST controller for deleting a list of variables within a data acquisition project.
 * 
 * @author Daniel Katzberg
 *
 */
@RestController
public class StudyDeleteResource {
  
  @Autowired 
  private StudyService studyService;
  
  @RequestMapping(path = "/api/studies/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllStudiesByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    studyService.deleteAllStudiesByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
