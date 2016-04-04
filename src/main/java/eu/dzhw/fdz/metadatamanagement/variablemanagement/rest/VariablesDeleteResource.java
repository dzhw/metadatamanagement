package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableService;

/**
 * REST controller for deleting a list of variables within a data acquisition project.
 *
 */
@RestController
public class VariablesDeleteResource {
  
  @Inject VariableService variableService;
  
  @RequestMapping(path = "/api/variables/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllSurveysByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    variableService.deleteVariablesByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
