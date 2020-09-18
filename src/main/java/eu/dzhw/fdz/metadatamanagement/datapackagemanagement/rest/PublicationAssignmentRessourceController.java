package eu.dzhw.fdz.metadatamanagement.datapackagemanagement.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.datapackagemanagement.domain.DataPackage;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.domain.RelatedPublication;
import eu.dzhw.fdz.metadatamanagement.relatedpublicationmanagement.service.RelatedPublicationManagementService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for managing the assignment of {@link RelatedPublication}s to
 * {@link DataPackage}s.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PublicationAssignmentRessourceController {

  private final RelatedPublicationManagementService relatedPublicationService;

  /**
   * Assign the given publication to the given dataPackage.
   * 
   * @param dataPackageId An id of a {@link DataPackage}.
   * @param publicationId An id of {@link RelatedPublication}.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @PutMapping(value = "/data-packages/{dataPackageId}/publications/{publicationId}")
  public ResponseEntity<?> assignPublicationToDataPackage(@PathVariable String dataPackageId,
      @PathVariable String publicationId) {
    relatedPublicationService.assignPublicationToDataPackage(dataPackageId, publicationId);
    return ResponseEntity.noContent().build();
  }

  /**
   * Remove the given publication from given dataPackage.
   * 
   * @param dataPackageId An id of a {@link DataPackage}.
   * @param publicationId An id of {@link RelatedPublication}.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-packages/{dataPackageId}/publications/{publicationId}")
  public ResponseEntity<?> removePublicationFromDataPackage(@PathVariable String dataPackageId,
      @PathVariable String publicationId) {
    relatedPublicationService.removePublicationFromDataPackage(dataPackageId, publicationId);
    return ResponseEntity.noContent().build();
  }
}
