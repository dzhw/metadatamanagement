package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetService;

/**
 * REST controller for deleting a list of dataSets within a data acquisition project.
 *
 */
@RestController
public class DataSetDeleteResource {
  
  @Autowired 
  private DataSetService dataSetService;
  
  @RequestMapping(path = "/api/data-sets/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllDataSetsByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    dataSetService.deleteDataSetsByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
