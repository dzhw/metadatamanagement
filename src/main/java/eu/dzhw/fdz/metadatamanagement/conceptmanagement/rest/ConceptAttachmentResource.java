package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.service.ConceptAttachmentService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for uploading an concept attachment.
 */
@Controller
@RequestMapping("/api")
public class ConceptAttachmentResource {

  @Autowired
  private ConceptAttachmentService conceptAttachmentService;

  /**
   * REST method for for uploading an concept attachment.
   * 
   * @param multiPartFile the attachment
   * @param conceptAttachmentMetadata the metadata for the attachment
   * @return response 201 if the attachment was created
   * @throws URISyntaxException if a URI is syntactically wrong
   * @throws IOException If the attachment cannot be read
   */
  @RequestMapping(path = "/concepts/attachments", method = RequestMethod.POST)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<String> uploadAttachment(@RequestPart("file") MultipartFile multiPartFile,
      @RequestPart("conceptAttachmentMetadata") 
      @Valid ConceptAttachmentMetadata conceptAttachmentMetadata)
      throws URISyntaxException, IOException {
    conceptAttachmentService.createConceptAttachment(multiPartFile,
        conceptAttachmentMetadata);
    return ResponseEntity.created(new URI(UriUtils.encodePath(
        conceptAttachmentMetadata.getId(), "UTF-8")))
        .body(null);
  }

  /**
   * Load all attachment metadata objects for the given concept id.
   * 
   * @param conceptId The id of a concept.
   * @return A list of metadata objects.
   */
  @RequestMapping(path = "/concepts/{conceptId}/attachments", method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> findByConceptId(@PathVariable("conceptId") String conceptId) {
    if (!StringUtils.isEmpty(conceptId)) {
      List<ConceptAttachmentMetadata> metadata =
          conceptAttachmentService.findAllByConcept(conceptId);
      return ResponseEntity.ok().cacheControl(CacheControl.noStore())
          .body(metadata);
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete all attachments of the given concept.
   * 
   * @param conceptId The id of the concept.
   */
  @RequestMapping(path = "/concepts/{conceptId}/attachments", method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> deleteAllByConceptId(@PathVariable("conceptId") String conceptId) {
    if (!StringUtils.isEmpty(conceptId)) {
      conceptAttachmentService.deleteAllByConceptId(conceptId);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Delete the given attachments of the given concept.
   * 
   * @param conceptId The id of the concept.
   * 
   */
  @RequestMapping(path = "/concepts/{conceptId}/attachments/{filename:.+}", 
      method = RequestMethod.DELETE)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> delete(@PathVariable("conceptId") String conceptId, 
      @PathVariable("filename") String filename) {
    if (!StringUtils.isEmpty(conceptId) && !StringUtils.isEmpty(filename)) {
      conceptAttachmentService.deleteByConceptIdAndFilename(conceptId, filename);
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.badRequest()
        .body(null);
    }
  }
  
  /**
   * Update the metadata of the given attachment of the given concept.
   */
  @RequestMapping(path = "/concepts/{conceptId}/attachments/{filename:.+}", 
      method = RequestMethod.PUT)
  @Secured(value = {AuthoritiesConstants.PUBLISHER, AuthoritiesConstants.DATA_PROVIDER})
  public ResponseEntity<?> update(
      @Valid @RequestBody ConceptAttachmentMetadata conceptAttachmentMetadata) {
    conceptAttachmentService.updateAttachmentMetadata(conceptAttachmentMetadata);
    return ResponseEntity.noContent().build();    
  }
}
