package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationService;

/**
 * REST controller for deleting a list of related publications within a data acquisition project.
 *
 *@author Daniel Katzberg
 */
@RestController
public class RelatedPublicationDeleteResource {
  
  @Inject 
  RelatedPublicationService relatedPublicationService;
  
  @RequestMapping(path = "/api/related-publications/delete-all", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAll() {
    this.relatedPublicationService.deleteAll();
    return ResponseEntity.ok().build();
  }
}
