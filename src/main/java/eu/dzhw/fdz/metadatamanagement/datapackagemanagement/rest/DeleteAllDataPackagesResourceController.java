package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageManagementService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for deleting dataPackages of a data acquisition project.
 *
 * @author tgehrke
 *
 */

@RestController
@RequiredArgsConstructor
public class DeleteAllDataPackagesResourceController {

  private final DataPackageManagementService dataPackageService;

  /**
   * delete all dataPackages from data acquisition project.
   *
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/api/data-acquisition-projects/{id}/data-packages")
  public ResponseEntity<?> delete(@PathVariable String id) {
    dataPackageService.deleteAllDataPackagesByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

