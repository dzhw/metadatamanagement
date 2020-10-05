package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyManagementService;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for retrieving all survey numbers available for creating
 * new surveys.
 * @author René Reitmann
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AvailableSurveyNumbersResourceController {
  
  private final SurveyManagementService surveyService;
   
  /**
   * Get all available survey numbers for the given project id.
   * 
   * @param id the projects id
   * @return the list of survey numbers
   */
  @RequestMapping(method = RequestMethod.GET, 
       value = "/data-acquisition-projects/{id:.+}/available-survey-numbers")
  public ResponseEntity<List<Integer>> findAvailableSurveyNumbers(@PathVariable String id) {
    List<Integer> surveyNumbers = surveyService.getFreeSurveyNumbers(id);
    return ResponseEntity.ok()
        .cacheControl(CacheControl.noStore())
        .body(surveyNumbers);
  }
}
