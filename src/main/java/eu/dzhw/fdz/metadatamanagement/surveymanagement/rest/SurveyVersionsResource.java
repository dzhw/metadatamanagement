package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyVersionsService;
import lombok.RequiredArgsConstructor;

/**
 * Rest Controller for retrieving previous version of the {@link Survey} domain object.
 * 
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SurveyVersionsResource {
  
  private final SurveyVersionsService surveyVersionsService;
    
  /**
   * Get the previous 5 versions of the survey.
   * 
   * @param id The id of the survey
   * @param limit like page size
   * @param skip for skipping n versions
   * 
   * @return A list of previous survey versions
   */
  @GetMapping("/surveys/{id}/versions")
  public ResponseEntity<?> findPreviousSurveyVersions(@PathVariable String id,
      @RequestParam(name = "limit", defaultValue = "5") Integer limit,
      @RequestParam(name = "skip", defaultValue = "0") Integer skip) {
    List<Survey> surveyVersions = surveyVersionsService.findPreviousVersions(id, limit, skip);
    
    if (surveyVersions == null) {
      return ResponseEntity.notFound().build();
    }
    
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(surveyVersions);
  }
}
