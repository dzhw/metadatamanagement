package eu.dzhw.fdz.metadatamanagement.instrumentmanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.domain.InstrumentAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.instrumentmanagement.service.InstrumentAttachmentVersionsService;

/**
 * Rest Controller for retrieving previous version of the {@link InstrumentAttachmentMetadata}
 * domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class InstrumentAttachmentVersionsResource {
  
  @Autowired
  private InstrumentAttachmentVersionsService instrumentAttachmentVersionsService;
    
  /**
   * Get the previous 10 versions of the instrument attachment metadata.
   * 
   * @param instrumentId The id of the instrument
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous instrument versions
   */
  @GetMapping("/instruments/{instrumentId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousInstrumentAttachmentVersions(
      @PathVariable String instrumentId,
      @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<InstrumentAttachmentMetadata> instrumentAttachmentVersions =
        instrumentAttachmentVersionsService.findPreviousInstrumentAttachmentVersions(instrumentId,
            filename, limit, skip);
    
    if (instrumentAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(instrumentAttachmentVersions);
  }
}
