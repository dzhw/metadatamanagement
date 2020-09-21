package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.repository.DataSetRepository;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller which retrieves all access way present in the primary data store for a given
 * dataPackage.
 * 
 * @author René Reitmann
 */
@RestController
@RequiredArgsConstructor
public class DataPackageAccessWaysResourceController {

  private final DataSetRepository dataSetRepository;

  /**
   * Get all available access ways of a data package.
   */
  @RequestMapping(value = "/api/data-packages/{dataPackageId}/access-ways",
      method = RequestMethod.GET)
  public ResponseEntity<List<String>> findAllAccessWays(@PathVariable String dataPackageId) {
    List<String> accessWays = dataSetRepository.findAllAccessWays(dataPackageId);
    return ResponseEntity.ok().cacheControl(CacheControl.noStore()).body(accessWays);
  }
}
