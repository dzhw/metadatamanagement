package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.QuestionService;
import eu.dzhw.fdz.metadatamanagement.usermanagement.security.AuthoritiesConstants;

/**
 * REST controller for deleting a list of Questions within a data acquisition project.
 *
 */
@RestController
@RequestMapping("/api")
public class QuestionDeleteResource {
  
  @Autowired 
  private QuestionService questionService;
  
  @RequestMapping(path = "/questions/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  @Secured(AuthoritiesConstants.PUBLISHER)
  public ResponseEntity<?> deleteQuestionsByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    questionService.deleteQuestionsByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
