package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
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
public class DaraReleaseResource {

  @Autowired
  private DaraService daraService;

  /**
   * POST /data-acquisition-projects/:id/relase -> Release a project to dara (or update it).
   * @throws TemplateException Template Errors of the XML Freemarker Process.
   * @throws IOException IO Exception for the XML Freemarker Process.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/release",
      method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<?> release(@PathVariable String id,
        @RequestBody @Valid DataAcquisitionProject project) throws IOException, TemplateException {
    HttpStatus status = this.daraService.registerOrUpdateProjectToDara(project);
    return ResponseEntity.status(status).build();
  }
}
