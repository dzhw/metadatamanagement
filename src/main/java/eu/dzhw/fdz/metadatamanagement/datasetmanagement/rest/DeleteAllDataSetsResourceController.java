package eu.dzhw.fdz.metadatamanagement.datasetmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datasetmanagement.domain.DataSet;
import eu.dzhw.fdz.metadatamanagement.datasetmanagement.service.DataSetManagementService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Rest Controller for deleting data sets of a data acquisition project.
 * 
 * @author tgehrke
 *
 */
@RestController
@RequestMapping("/api")
public class DeleteAllDataSetsResourceController {
  @Autowired
  private DataSetManagementService dataSetService;

  /**
   * delete all data sets from data acquisition project.
   * 
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/data-sets")
  public ResponseEntity<DataSet> delete(@PathVariable String id) {
    dataSetService.deleteDataSetsByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

