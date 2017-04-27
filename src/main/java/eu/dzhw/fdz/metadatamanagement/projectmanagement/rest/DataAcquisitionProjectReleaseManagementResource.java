package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;

/**
 * A Resource Class for handling releasing and unreleading of data acquisition projects to dara.
 * This Resource can register a DOI to dara, updates information of a dara doi or set to not 
 * available if a project will be unreleased.
 * 
 * @author Daniel Katzberg
 *
 */
@RestController
@RequestMapping("/api")
public class DataAcquisitionProjectReleaseManagementResource {
  
  @Autowired
  private DaraService daraService;
  
  /**
   * POST /data-acquisition-projects/:id/relase -> Release a project to dara (or update it).
   * @throws TemplateException Template Errors of the XML Freemarker Process.
   * @throws IOException IO Exception for the XML Freemarker Process.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/release",
      method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> release(@PathVariable String id) throws IOException, TemplateException {
    HttpStatus status = this.daraService.registerOrUpdateProjectToDara(id);
    return ResponseEntity.status(status).build();
  }
  
  
  /**
   * POST /data-acquisition-projects/:id/unrelase -> Unrelease a project to dara 
   * (set status to not available).
   * @throws TemplateException Template Errors of the XML Freemarker Process.
   * @throws IOException IO Exception for the XML Freemarker Process. 
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/unrelease",
      method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> unrelease(@PathVariable String id) 
      throws IOException, TemplateException {
    HttpStatus status = this.daraService.unregisterProjectToDara(id);
    return ResponseEntity.status(status).build();
  }

}
