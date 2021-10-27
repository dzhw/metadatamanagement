package eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.analysispackagemanagement.service.AnalysisPackageManagementService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for deleting analysis packages of a data acquisition project.
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteAllAnalysisPackagesResourceController {

  private final AnalysisPackageManagementService analysisPackageService;

  /**
   * delete all analysis packages from data acquisition project.
   *
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/analysis-packages")
  public ResponseEntity<?> delete(@PathVariable String id) {
    analysisPackageService.deleteAnalysisPackagesByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

