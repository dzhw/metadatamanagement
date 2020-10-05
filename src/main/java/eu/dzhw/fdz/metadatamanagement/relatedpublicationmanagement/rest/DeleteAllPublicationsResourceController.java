package eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationManagementService;
import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for removing all publications from a dataPackage.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DeleteAllPublicationsResourceController {

  private final RelatedPublicationManagementService relatedPublicationService;

  /**
   * Remove all publication from the given dataPackage.
   *
   * @param id the id of the {@link DataPackage}.
   *
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-packages/{id}/publications")
  public ResponseEntity<?> delete(@PathVariable String id) {
    relatedPublicationService.removeAllPublicationsFromDataPackage(id);
    return ResponseEntity.noContent().build();
  }
}
