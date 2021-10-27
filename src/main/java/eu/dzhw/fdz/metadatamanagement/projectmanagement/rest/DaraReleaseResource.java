package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorDto;
import eu.dzhw.fdz.metadatamanagement.common.rest.errors.ErrorListDto;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.DataAcquisitionProject;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.domain.ShadowCopyReleaseToDaraNotAllowed;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.DaraService;
import eu.dzhw.fdz.metadatamanagement.authmanagement.security.AuthoritiesConstants;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class DaraReleaseResource {

  private final DaraService daraService;

  /**
   * Release a project to dara (or update it).
   * @throws TemplateException Template Errors of the XML Freemarker Process.
   * @throws IOException IO Exception for the XML Freemarker Process.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/release",
      method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER})
  public ResponseEntity<?> release(@PathVariable String id,
        @RequestBody @Valid DataAcquisitionProject project) throws IOException, TemplateException {
    if (project.isShadow()) {
      throw new ShadowCopyReleaseToDaraNotAllowed();
    }
    HttpStatus status = this.daraService.registerOrUpdateProjectToDara(project);
    return ResponseEntity.status(status).build();
  }

  @ExceptionHandler(ShadowCopyReleaseToDaraNotAllowed.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected ErrorListDto handleShadowCopyReleaseToDaraNotAllowed() {
    ErrorDto errorDto = new ErrorDto(null, "project-management.error."
        + "shadow-copy-release-to-dara-not-allowed", null, null);
    ErrorListDto errorListDto = new ErrorListDto();
    errorListDto.add(errorDto);
    return errorListDto;
  }
}
