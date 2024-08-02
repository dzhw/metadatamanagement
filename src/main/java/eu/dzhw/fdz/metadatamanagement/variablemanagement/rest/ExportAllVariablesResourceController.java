package eu.dzhw.fdz.metadatamanagement.variablemanagement.rest;

import java.io.IOException;

import javax.persistence.PersistenceException;

import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import eu.dzhw.fdz.metadatamanagement.variablemanagement.service.VariableManagementService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for exporting all variables for PID registration.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExportAllVariablesResourceController {

  private final VariableManagementService variablesManagementService;

  /**
   * export all variables for PID registration.
   *
   * @return a JSON file
   */
  @PostMapping(value = "/variables/exportAll", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  @Secured(value = {AuthoritiesConstants.ADMIN})
  public ResponseEntity<?> exportAllVariables() {
    ResponseEntity<?> response = new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    try {
      response = this.variablesManagementService.exportVariablesAsJson();
    } catch (PersistenceException | IOException ex) {
      return response;
    }

    return response;
  }


}
