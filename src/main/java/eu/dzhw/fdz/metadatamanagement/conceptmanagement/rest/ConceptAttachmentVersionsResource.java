package eu.dzhw.fdz.metadatamanagement.conceptmanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.Concept;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.domain.ConceptAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.conceptmanagement.service.ConceptAttachmentVersionsService;

/**
 * Rest Controller for retrieving previous version of the 
 * {@link ConceptAttachmentMetadata} domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class ConceptAttachmentVersionsResource {
  
  @Autowired
  private ConceptAttachmentVersionsService conceptAttachmentVersionsService;
    
  /**
   * Get the previous 10 versions of the {@link ConceptAttachmentMetadata}.
   * 
   * @param conceptId The id of the {@link Concept}
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous concept attachment versions
   */
  @GetMapping("/concepts/{conceptId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousConceptAttachmentVersions(@PathVariable String conceptId,
      @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<ConceptAttachmentMetadata> conceptAttachmentVersions = conceptAttachmentVersionsService
        .findPreviousConceptAttachmentVersions(conceptId, filename, limit, skip);
    
    if (conceptAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(conceptAttachmentVersions);
  }
}
