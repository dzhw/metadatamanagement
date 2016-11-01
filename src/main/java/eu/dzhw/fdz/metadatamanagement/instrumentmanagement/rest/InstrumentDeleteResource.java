package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentService;

/**
 * REST controller for deleting all instruments within a data acquisition project.
 * 
 * @author René Reitmann
 *
 */
@RestController
public class InstrumentDeleteResource {
  
  @Inject 
  private InstrumentService instrumentService;
  
  @RequestMapping(path = "/api/instruments/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllStudiesByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    instrumentService.deleteAllInstrumentsByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
