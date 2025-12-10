package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageDdiService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for exporting variable metadata as DDI codebook.
 */
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExportDdiVariablesResourceController {

  private final DataPackageDdiService dataPackageDdiService;

  /**
   * Exports all variables metadata belonging to the given dataPackage ID as DDI Codebook XML file.
   *
   * @return an XML file
   */
  @GetMapping(value = "/data-packages/exportDDI/xml/{dataPackageId:.+}", produces = MediaType.APPLICATION_XML_VALUE)
  @ResponseBody
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> exportVariablesAsXml(@PathVariable String dataPackageId) {
    return this.dataPackageDdiService.exportDdiVariablesAsXml(dataPackageId);
  }

}
