package eu.dzhw.fdz.metadatamanagement.surveymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.service.SurveyService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Rest Controller for deleting surveys of a data acquisition project.
 * 
 * @author tgehrke
 *
 */

@RestController
@RequestMapping("/api")
public class DeleteAllSurveysResourceController {
  @Autowired
  private SurveyService surveyService;

  /**
   * delete all surveys from data acquisition project.
   * 
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER,
      AuthoritiesConstants.ADMIN})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/surveys")
  public ResponseEntity<Survey> deleteAllMetadataByType(@PathVariable String id) {
    surveyService.deleteAllSurveysByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

