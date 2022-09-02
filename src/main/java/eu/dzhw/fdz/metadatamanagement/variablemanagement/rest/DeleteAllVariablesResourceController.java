package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.domain.Variable;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableManagementService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for deleting variables of a data acquisition project.
 *
 * @author tgehrke
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteAllVariablesResourceController {

  private final VariableManagementService variableService;

  /**
   * delete all variables from data acquisition project.
   *
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/variables")
  public ResponseEntity<Variable> delete(@PathVariable String id) {
    variableService.deleteAllVariablesByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

