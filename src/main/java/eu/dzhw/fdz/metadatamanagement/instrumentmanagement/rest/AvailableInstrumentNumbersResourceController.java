package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentService;

/**
 * REST Controller for retrieving all instrument numbers available for creating new instruments.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class AvailableInstrumentNumbersResourceController {
  
  @Autowired
  private InstrumentService instrumentService;
   
  /**
   * Get all available instrument numbers for the given project id.
   * 
   * @param id the projects id
   * @return the list of instrument numbers
   */
  @RequestMapping(method = RequestMethod.GET, 
      value = "/data-acquisition-projects/{id:.+}/available-instrument-numbers")
  public ResponseEntity<List<Integer>> findAvailableInstrumentNumbers(@PathVariable String id) {
    if (StringUtils.isEmpty(id)) {
      return ResponseEntity.badRequest().build();
    }
    List<Integer> instrumentNumbers = instrumentService.getFreeInstrumentNumbers(id);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(instrumentNumbers);
  }
}
