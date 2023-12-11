package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationErrorsDto;
import eu.dzhw.fdz.metadatamanagement.projectmanagement.service.PostValidationService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A Resource class for the post validation of data acquisition projects. It get the id of the
 * project by the client and called url. Post validation means that all data, collection and foreign
 * keys will be check for valid values. The Post-Validation returns a list of errors, wrapped by a
 * DTO class.
 *
 * @author Daniel Katzberg
 *
 */
@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DataAcquisitionProjectPostValidationResource {

  private final PostValidationService postValidationService;

  /**
   * Validate project by id.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/post-validate",
      method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<PostValidationErrorsDto> postValidate(@PathVariable String id,
      @RequestParam(required = false) String version) {
    log.debug("REST request for post validation : {}", id);
    return new ResponseEntity<>(
        new PostValidationErrorsDto(this.postValidationService.postValidate(id, version)),
        HttpStatus.OK);
  }

  /**
   * Validate project by id.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/post-validate-pre-release",
    method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<PostValidationErrorsDto> postValidatePreRelease(@PathVariable String id) {
    log.debug("REST request for post validation : {}", id);
    return new ResponseEntity<>(
      new PostValidationErrorsDto(this.postValidationService.postValidatePreRelease(id)),
      HttpStatus.OK);
  }
}
