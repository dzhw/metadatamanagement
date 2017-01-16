package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.projectmanagement.rest.dto.PostValidationErrorsDto;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationPostValidationService;

/**
 * A Resource class for the post validation of related publications. All related publications will 
 * be checked. Post validation means that all foreign
 * keys will be check for valid values. Semantic validations are included, too. One example for a
 * semantic validation is that all domain object have to be the same study id like the related
 * publication. The Post-Validation returns a list of errors, wrapped by a DTO class.
 * 
 * @author Daniel Katzberg
 *
 */
@RestController
@RequestMapping("/api")
public class RelatedPublicationPostValidationResource {

  @Autowired
  private RelatedPublicationPostValidationService postValidationService;

  /**
   * POST /related-publications/post-validate -> Validate all related publications.
   */
  @RequestMapping(value = "/related-publications/post-validate",
      method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<PostValidationErrorsDto> postValidate() {
    return new ResponseEntity<>(
          new PostValidationErrorsDto(
            this.postValidationService.postValidate()),
        HttpStatus.OK);
  }
}
