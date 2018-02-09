package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.SurveyAttachmentMetadata;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyAttachmentVersionsService;

/**
 * Rest Controller for retrieving previous version of the 
 * {@link SurveyAttachmentMetadata} domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
public class SurveyAttachmentVersionsResource {
  
  @Autowired
  private SurveyAttachmentVersionsService surveyAttachmentVersionsService;
    
  /**
   * Get the previous 10 versions of the survey attachment metadata.
   * 
   * @param surveyId The id of the survey
   * @param filename The filename of the attachment
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous survey versions
   */
  @RequestMapping("/surveys/{surveyId}/attachments/{filename:.+}/versions")
  @Timed
  public ResponseEntity<?> findPreviousSurveyAttachmentVersions(@PathVariable String surveyId,
      @PathVariable String filename,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<SurveyAttachmentMetadata> surveyAttachmentVersions = surveyAttachmentVersionsService
        .findPreviousSurveyAttachmentVersions(surveyId, filename, limit, skip);
    
    if (surveyAttachmentVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(surveyAttachmentVersions);
  }
}
