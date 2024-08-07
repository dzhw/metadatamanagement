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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for exporting variable metadata as DDI codebook.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExportDdiVariablesResourceController {

  private final VariableManagementService variablesManagementService;

  /**
   * Exports all variables metadata belonging to the given survey ID as DDI Codebook.
   *
   * @return an XML file
   */
  @GetMapping(value = "/variables/exportDDI/xml/{studyId:.+}", produces = MediaType.APPLICATION_XML_VALUE)
  @ResponseBody
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> exportVariablesAsXml(@PathVariable String studyId) {
    ResponseEntity<?> response = new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    try {
      response = this.variablesManagementService.exportDdiVariablesAsXML(studyId);
    } catch (PersistenceException ex) {
      return response;
    }

    return response;
  }

}
