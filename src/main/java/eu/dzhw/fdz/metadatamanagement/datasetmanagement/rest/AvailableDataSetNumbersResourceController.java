package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetManagementService;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for retrieving all dataSet numbers available for creating new dataSets.
 *
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvailableDataSetNumbersResourceController {

  private final DataSetManagementService dataSetService;

  /**
   * Get all available dataSet numbers for the given project id.
   *
   * @param id the projects id
   * @return the list of dataSet numbers
   */
  @RequestMapping(method = RequestMethod.GET,
      value = "/data-acquisition-projects/{id:.+}/available-data-set-numbers")
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
