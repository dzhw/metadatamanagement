package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.PersistenceException;
import javax.xml.bind.JAXBException;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.service.DataPackageDdiService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
@Slf4j
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
    return buildXmlResponse(dataPackageId);
  }

  @GetMapping(value = "/data-packages/exportDDI/zip/all", produces = "application/zip")
  @ResponseBody
  public ResponseEntity<?> exportAllVariablesAsZip() {
    try {
      byte[] xml = dataPackageDdiService.buildDdiXml("stu-cmp2014$");
      ByteArrayOutputStream buf = new ByteArrayOutputStream();
      try (ZipOutputStream zip = new ZipOutputStream(buf)) {
        zip.putNextEntry(new ZipEntry("Variables_DDI_MDM_Export.xml"));
        zip.write(xml);
        zip.closeEntry();
      }
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Variables_DDI_MDM_Export.zip");
      return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(buf.toByteArray()));
    } catch (JAXBException | PersistenceException | IOException ex) {
      log.error("Error generating DDI ZIP: {}", ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private ResponseEntity<?> buildXmlResponse(String dataPackageId) {
    try {
      byte[] xml = dataPackageDdiService.buildDdiXml(dataPackageId);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Variables_DDI_MDM_Export.xml");
      return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(xml));
    } catch (JAXBException | PersistenceException ex) {
      log.error("Error generating DDI XML for {}: {}", dataPackageId, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

}
