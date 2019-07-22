package eu.dzhw.fdz.metadatamanagement.studymanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.dzhw.fdz.metadatamanagement.studymanagement.service.StudyManagementService;
import eu.dzhw.fdz.metadatamanagement.surveymanagement.domain.Survey;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * Rest Controller for deleting studies of a data acquisition project.
 * 
 * @author tgehrke
 *
 */

@RestController
@RequestMapping("/api")
public class DeleteAllStudiesResourceController {
  @Autowired
  private StudyManagementService studyService;

  /**
   * delete all studies from data acquisition project.
   * 
   * @param id the Id of the project.
   * @return no Content.
   */
  @Secured(value = {AuthoritiesConstants.DATA_PROVIDER, AuthoritiesConstants.PUBLISHER})
  @DeleteMapping(value = "/data-acquisition-projects/{id}/studies")
  public ResponseEntity<Survey> delete(@PathVariable String id) {
    studyService.deleteAllStudiesByProjectId(id);
    return ResponseEntity.noContent().build();
  }
}

