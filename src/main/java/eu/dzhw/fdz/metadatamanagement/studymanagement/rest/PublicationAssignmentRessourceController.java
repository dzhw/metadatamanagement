package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationManagementService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Rest Controller for managing the assignment of {@link RelatedPublication}s to {@link Study}s.
 */
@RestController
@RequestMapping("/api")
public class PublicationAssignmentRessourceController {
  @Autowired
  private RelatedPublicationManagementService relatedPublicationService;

  /**
   * Assign the given publication to the given study.
   * 
   * @param studyId An id of a {@link Study}.
   * @param publicationId An id of {@link RelatedPublication}.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @PutMapping(value = "/studies/{studyId}/publications/{publicationId}")
  public ResponseEntity<?> assignPublicationToStudy(@PathVariable String studyId,
      @PathVariable String publicationId) {
    relatedPublicationService.assignPublicationToStudy(studyId, publicationId);
    return ResponseEntity.noContent().build();
  }
  
  /**
   * Remove the given publication from given study.
   * 
   * @param studyId An id of a {@link Study}.
   * @param publicationId An id of {@link RelatedPublication}.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/studies/{studyId}/publications/{publicationId}")
  public ResponseEntity<?> removePublicationFromStudy(@PathVariable String studyId,
      @PathVariable String publicationId) {
    relatedPublicationService.removePublicationFromStudy(studyId, publicationId);
    return ResponseEntity.noContent().build();
  }
}
