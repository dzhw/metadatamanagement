package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetService;

/**
 * REST Controller for retrieving all dataSet numbers available for creating new dataSets.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class AvailableDataSetNumbersResourceController {
  
  @Autowired
  private DataSetService dataSetService;
   
  /**
   * Get all available dataSet numbers for the given project id.
   * 
   * @param id the projects id
   * @return the list of dataSet numbers
   */
  @RequestMapping(method = RequestMethod.GET, 
      value = "/data-acquisition-projects/{id:.+}/available-dataSet-numbers")
  public ResponseEntity<List<Integer>> findAvailableDataSetNumbers(@PathVariable String id) {
    if (StringUtils.isEmpty(id)) {
      return ResponseEntity.badRequest().build();
    }
    List<Integer> dataSetNumbers = dataSetService.getFreeDataSetNumbers(id);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(dataSetNumbers);
  }
}
