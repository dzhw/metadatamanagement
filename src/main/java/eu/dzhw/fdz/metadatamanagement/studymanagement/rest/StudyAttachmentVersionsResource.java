package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.studymanagement.domain.StudyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyAttachmentVersionsService;

/**
 * Rest Controller for retrieving previous version of the 
 * {@link StudyAttachmentMetadata} domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class StudyAttachmentVersionsResource {
  
  @Autowired
  private StudyAttachmentVersionsService studyAttachmentVersionsService;
    
  /**
   * Get the previous 10 versions of the study attachment metadata.
   * 
   * @param studyId The id of the study
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous study versions
   */
  @RequestMapping("/studies/{studyId}/attachments/{filename:.+}/versions")
  public ResponseEntity<?> findPreviousStudyVersions(@PathVariable String studyId,
      @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<StudyAttachmentMetadata> studyAttachmentVersions = studyAttachmentVersionsService
        .findPreviousStudyAttachmentVersions(studyId, filename, limit, skip);
    
    if (studyAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(studyAttachmentVersions);
  }
}
