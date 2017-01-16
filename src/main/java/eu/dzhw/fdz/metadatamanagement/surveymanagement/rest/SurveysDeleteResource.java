package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyService;

/**
 * REST controller for deleting a list of surveys within a data acquisition project.
 *
 */
@RestController
public class SurveysDeleteResource {
  
  @Autowired 
  private SurveyService surveyService;
  
  @RequestMapping(path = "/api/surveys/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllSurveysByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    surveyService.deleteAllSurveysByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
