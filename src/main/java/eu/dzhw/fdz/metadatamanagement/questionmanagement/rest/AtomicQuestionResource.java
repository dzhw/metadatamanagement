package eu.dzhw.fdz.metadatamanagement.questionmanagement.rest;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import eu.dzhw.fdz.metadatamanagement.questionmanagement.service.AtomicQuestionService;

/**
 * REST controller for deleting a list of AtomicQuestions within a data acquisition project.
 *
 */
@RestController
public class AtomicQuestionResource {
  
  @Inject AtomicQuestionService atomicQuestionService;
  
  @RequestMapping(path = "/api/atomic-questions/delete", method = RequestMethod.POST,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @Timed
  public ResponseEntity<?> deleteAllSurveysByProjectId(
      @RequestParam String dataAcquisitionProjectId) {
    atomicQuestionService.deleteAtomicQuestionsByProjectId(dataAcquisitionProjectId);
    return ResponseEntity.ok().build();
  }
}
