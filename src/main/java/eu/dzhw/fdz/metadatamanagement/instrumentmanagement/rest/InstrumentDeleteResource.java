package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for deleting all instruments within a data acquisition project.
 * 
 * @author René Reitmann
 *
 */
@RestController
public class InstrumentDeleteResource {
  
  @Autowired 
  private InstrumentService instrumentService;
  
  @RequestMapping(path = "/api/instruments/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> deleteAllStudiesByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    instrumentService.deleteAllInstrumentsByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
