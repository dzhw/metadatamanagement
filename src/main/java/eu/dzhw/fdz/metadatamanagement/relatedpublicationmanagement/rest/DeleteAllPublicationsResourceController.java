package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationManagementService;
import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.Study;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for removing all publications from a study.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteAllPublicationsResourceController {
  
  private final RelatedPublicationManagementService relatedPublicationService;

  /**
   * Remove all publication from the given study. 
   * 
   * @param id the id of the {@link Study}.
   * 
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/studies/{id}/publications")
  public ResponseEntity<?> delete(@PathVariable String id) {
    relatedPublicationService.removeAllPublicationsFromStudy(id);
    return ResponseEntity.noContent().build();
  }
}
