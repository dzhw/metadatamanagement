package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for deleting a list of related publications within a data acquisition project.
 *
 *@author Daniel Katzberg
 */
@RestController
public class RelatedPublicationDeleteResource {
  
  @Autowired 
  private RelatedPublicationService relatedPublicationService;
  
  @RequestMapping(path = "/api/related-publications/delete-all", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> deleteAll() {
    this.relatedPublicationService.deleteAll();
    return ResponseEntity.ok().build();
  }
}
