package eu.dzhw.fdz.metadatamanagement.projectmanagement.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationErrorsDto;

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
public class DataAcquisitionProjectPostValidationResource {

  private final Logger log =
      LoggerFactory.getLogger(DataAcquisitionProjectPostValidationResource.class);

  /**
   * GET /users/:login -> get the "login" user.
   */
  @RequestMapping(value = "/data-acquisition-projects/{id}/post-validate",
      method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<PostValidationErrorsDto> getUser(@PathVariable String id) {
    log.debug("REST request for post validation : {}", id);
    return null;
  }
}
