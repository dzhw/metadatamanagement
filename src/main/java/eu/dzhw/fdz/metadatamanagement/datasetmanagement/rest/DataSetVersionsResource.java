package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetVersionsService;

/**
 * Rest Controller for retrieving previous version of the dataSet domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class DataSetVersionsResource {

  @Autowired
  private DataSetVersionsService dataSetVersionsService;

  /**
   * Get the previous 5 versions of the dataSet.
   * 
   * @param id The id of the dataSet
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous dataSet versions
   */
  @GetMapping("/data-sets/{id}/versions")
  public ResponseEntity<?> findPreviousDataSetVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<DataSet> dataSetVersions = dataSetVersionsService.findPreviousVersions(id, limit, skip);

    if (dataSetVersions == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(dataSetVersions);
  }
}
